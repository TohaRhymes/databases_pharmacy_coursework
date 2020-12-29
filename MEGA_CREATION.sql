drop table if exists development;

drop table if exists stock;

drop table if exists trademarks;

drop table if exists patents;

drop table if exists pharmacies;

drop table if exists drugs_to_diseases;

drop table if exists drugs_to_poisons;

drop table if exists poisons;

drop table if exists drugs;

drop table if exists ethnoscience_to_diseases;

drop table if exists diseases;

drop table if exists pathogens;

drop table if exists ethnoscience;

drop table if exists company_info;

drop table if exists companies;



drop type if exists pathogen_type cascade;

drop type if exists drugs_groups cascade;

drop type if exists poison_origin cascade;

drop type if exists development_stage cascade;

drop type if exists stock_availability cascade;

drop type if exists patent_distribution cascade;



CREATE TYPE pathogen_type AS ENUM ('virus', 'bacterium', 'protozoan', 'prion', 'viroid', 'fungus', 'small animal');
CREATE TYPE drugs_groups AS ENUM ('Group A (prohibited substances)', 'Group B (limited turnover)', 'Group C (free circulation)');
CREATE TYPE poison_origin AS ENUM ('nature', 'chemicals', 'synthetic');
CREATE TYPE development_stage AS ENUM ('Preclinical phase', 'Phase 0', 'Phase I', 'Phase II', 'Phase III', 'Phase IV');
CREATE TYPE patent_distribution AS ENUM ('free-to-use', 'usage with some constraints', 'restricted-to-use');


CREATE TABLE pathogens
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(80)   NOT NULL,
    type   pathogen_type NOT NULL,
    action VARCHAR(80)   NOT NULL
);

CREATE TABLE diseases
(
    id          SERIAL PRIMARY KEY,
    pathogen_id INTEGER
        CONSTRAINT fk_pathogens_id REFERENCES pathogens (id) ON DELETE CASCADE,
    name        VARCHAR(80),
    mortality   DECIMAL NOT NULL
        DEFAULT 0 CHECK ( mortality >= 0 and mortality <= 1 )
);

CREATE TABLE poisons
(
    id               SERIAL PRIMARY KEY,
    active_substance VARCHAR(80) UNIQUE NOT NULL,
    type_by_action   VARCHAR(80)        NOT NULL,
    type_by_origin   poison_origin,
    mortality        DECIMAL            NOT NULL
        DEFAULT 0 CHECK ( mortality >= 0 and mortality <= 1 )
);

CREATE TABLE ethnoscience
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(80) NOT NULL,
    origin VARCHAR(80) NOT NULL
);

CREATE TABLE companies
(
    id                           SERIAL PRIMARY KEY,
    name                         VARCHAR(80) UNIQUE NOT NULL,
    specialization               VARCHAR(80)        NOT NULL,
    market_cap                   DECIMAL CHECK ( market_cap >= 0 ),
    net_profit_margin_pct_annual DECIMAL CHECK ( net_profit_margin_pct_annual >= 0 ) -- ЧЕ это?))))
);

CREATE TABLE company_info
(
    id                           SERIAL PRIMARY KEY,
    company_id                   INTEGER
        CONSTRAINT fk_companies_id REFERENCES companies (id) ON DELETE CASCADE
        NOT NULL,
    restore_date                 TIMESTAMP,
    market_cap                   DECIMAL CHECK ( market_cap >= 0 ),
    net_profit_margin_pct_annual DECIMAL CHECK ( net_profit_margin_pct_annual >= 0 )
);
CREATE TABLE drugs
(
    id               SERIAL PRIMARY KEY,
    active_substance VARCHAR(80) UNIQUE NOT NULL,
    homeopathy       BOOLEAN            NOT NULL
        DEFAULT false,
    drugs_group      drugs_groups       NOT NULL
);


CREATE TABLE development
(
    id            SERIAL PRIMARY KEY,
    company_id    INTEGER
        CONSTRAINT fk_companies_id
            REFERENCES companies (id) ON DELETE CASCADE NOT NULL,
    pathogen_id   INTEGER
        CONSTRAINT fk_pathogens_id
            REFERENCES pathogens (id) ON DELETE CASCADE NOT NULL,
    testing_stage development_stage                     NOT NULL,
    failed        BOOLEAN                               NOT NULL DEFAULT false
);

CREATE TABLE patents
(
    id           SERIAL PRIMARY KEY,
    distribution patent_distribution NOT NULL,
    start_date   DATE                NOT NULL
);

CREATE TABLE trademarks
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(80) UNIQUE NOT NULL,
    doze          DECIMAL            NOT NULL CHECK ( doze > 0 ),
    release_price DECIMAL            NOT NULL check ( release_price > 0 ),
    drug_id       INTEGER
        CONSTRAINT fk_drugs_id
            REFERENCES drugs (id) ON DELETE CASCADE,
    company_id    INTEGER
        CONSTRAINT fk_companies_id
            REFERENCES companies (id) ON DELETE CASCADE
                                     NOT NULL,
    patent_id     INTEGER
        CONSTRAINT fk_patents_patent_id
            REFERENCES patents (id) ON DELETE CASCADE,
    UNIQUE (drug_id, company_id, patent_id)
);

CREATE TABLE pharmacies
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(80) NOT NULL,
    price_mul  DECIMAL     NOT NULL CHECK ( price_mul >= 1 ),
    price_plus DECIMAL     NOT NULL CHECK ( price_plus >= 0 )
);

CREATE TABLE stock
(
    id           SERIAL PRIMARY KEY,
    pharmacy_id  INTEGER
        CONSTRAINT fk_pharmacy_id REFERENCES pharmacies (id) ON DELETE CASCADE
        NOT NULL,
    trademark_id INTEGER
        CONSTRAINT fk_trademarks_id REFERENCES trademarks (id) ON DELETE CASCADE
        NOT NULL,
    availability INTEGER DEFAULT 0 CHECK (availability >= 0),
    UNIQUE (pharmacy_id, trademark_id)
);

CREATE TABLE drugs_to_diseases
(
    id          SERIAL PRIMARY KEY,
    drugs_id   INTEGER
        CONSTRAINT fk_drugs_id REFERENCES drugs (id) ON DELETE CASCADE
        NOT NULL,
    disease_id INTEGER
        CONSTRAINT fk_diseases_id REFERENCES diseases (id) ON DELETE CASCADE
        NOT NULL,
    UNIQUE (drugs_id, disease_id)
);
CREATE TABLE drugs_to_poisons
(
    id          SERIAL PRIMARY KEY,
    drugs_id  INTEGER
        CONSTRAINT fk_drugs_id REFERENCES drugs (id) ON DELETE CASCADE
        NOT NULL,
    poison_id INTEGER
        CONSTRAINT fk_poisons_id REFERENCES poisons (id) ON DELETE CASCADE
        NOT NULL,
    UNIQUE (drugs_id, poison_id)
);
CREATE TABLE ethnoscience_to_diseases
(
    id          SERIAL PRIMARY KEY,
    ethnoscience_id INTEGER
        CONSTRAINT fk_ethnoscience_id REFERENCES ethnoscience (id) ON DELETE CASCADE
        NOT NULL,
    disease_id      INTEGER
        CONSTRAINT fk_diseases_id REFERENCES diseases (id) ON DELETE CASCADE
        NOT NULL,
    UNIQUE (ethnoscience_id, disease_id)
);



CREATE OR REPLACE FUNCTION process_company_info() RETURNS TRIGGER AS
$company_info$
BEGIN
    --
    -- Добавление строки в company_info, которая отражает новую запись в company;
    --
    INSERT INTO company_info(company_id, restore_date, market_cap, net_profit_margin_pct_annual)
    SELECT NEW.id, now(), NEW.market_cap, NEW.net_profit_margin_pct_annual;
    RETURN NEW;
END ;
$company_info$ LANGUAGE plpgsql;

drop TRIGGER IF EXISTS company_info_T on companies;

CREATE TRIGGER company_info_T
    AFTER INSERT OR UPDATE
    on companies
    FOR EACH ROW
EXECUTE PROCEDURE process_company_info();

-----------------------------------------------------------


CREATE OR REPLACE FUNCTION patent_date() RETURNS trigger AS
$patent_date$
BEGIN
    -- Проверить, что указана дата
    IF NEW.start_date IS NULL THEN
        NEW.start_date := now();
    END IF;
    RETURN NEW;
END;
$patent_date$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS patent_date_T ON patents;

CREATE TRIGGER patent_date_T
    BEFORE INSERT OR UPDATE
    ON patents
    FOR EACH ROW
EXECUTE PROCEDURE patent_date();

-----------------------------------------------------------


CREATE OR REPLACE FUNCTION stock_availability() RETURNS trigger AS
$stock_availability$
BEGIN
    IF NEW.availability IS NULL THEN
        NEW.availability := 0;
        RAISE NOTICE 'Supposed, that this trademark is empty!';
    END IF;
    RETURN NEW;
END;
$stock_availability$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS stock_availability_T on stock;

CREATE TRIGGER stock_availability_T
    BEFORE INSERT OR UPDATE
    ON stock
    FOR EACH ROW
EXECUTE PROCEDURE stock_availability();

-----------------------------------------------------------


CREATE OR REPLACE FUNCTION stock_msg_last() RETURNS trigger AS
$stock_msg_last$
BEGIN
    IF NEW.availability = 0 THEN
        RAISE NOTICE 'Bought the last pack of treatment!';
    END IF;
    RETURN NEW;
END;
$stock_msg_last$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS stock_msg_last_T on stock;

CREATE TRIGGER stock_msg_last_T
    AFTER UPDATE
    ON stock
    FOR EACH ROW
EXECUTE PROCEDURE stock_msg_last();


-- OTHER FUNCTIONS


-- Function for changing current economic indicators of the company
-- BUT FIRST returns random in range: 0.95-1.15
DROP FUNCTION IF EXISTS random_changing();

CREATE OR REPLACE FUNCTION random_changing()
    RETURNS DECIMAL AS
$$
BEGIN
    RETURN (SELECT 1 + random() * 0.2 - 0.05 AS random_changing);
END;
$$ LANGUAGE plpgsql VOLATILE;

-- USAGE EXAMPLE;
-- SELECT *
-- FROM random_changing();

--AND now function for changing
DROP FUNCTION IF EXISTS change_economical(_company_id int);

CREATE OR REPLACE FUNCTION change_economical(_company_id int)
    RETURNS TABLE
            (
                idd INTEGER,
                n   VARCHAR(80),
                s   VARCHAR(80),
                m_c DECIMAL,
                np  DECIMAL
            )
--     RETURNS void
AS
$$
DECLARE
    a DECIMAL := (SELECT SUM(random_changing)
                  FROM random_changing());
    b DECIMAL := (SELECT SUM(random_changing)
                  FROM random_changing());
    c DECIMAL := (SELECT SUM(random_changing)
                  FROM random_changing());
    d DECIMAL := (SELECT SUM(random_changing)
                  FROM random_changing());
BEGIN
    UPDATE companies cc
    SET (market_cap, net_profit_margin_pct_annual) = (mc * a, npmpa * c)
    FROM (SELECT id idd, market_cap mc, net_profit_margin_pct_annual npmpa
          FROM companies c
          WHERE (c.id = _company_id)
         ) AS prev_val
    WHERE cc.id = _company_id;

    RETURN QUERY (SELECT comp.id                           idd,
                         comp.name                         n,
                         comp.specialization               s,
                         comp.market_cap                   m_c,
                         comp.net_profit_margin_pct_annual np
                  FROM companies comp
                  WHERE comp.id = _company_id);
END;
$$ LANGUAGE plpgsql VOLATILE;

-- USAGE
-- SELECT *
-- FROM change_economical(4);

-----------------------------------------------------------


-- Function for buying trademarks (new, or existing ones)
DROP FUNCTION IF EXISTS add_to_stock(_pharmacy_id int, _trademark_id int, _availability int);

CREATE OR REPLACE FUNCTION add_to_stock(_pharmacy_id int,
                                        _trademark_id int,
                                        _availability int)
    RETURNS int AS
$$
DECLARE
    amount INTEGER;
BEGIN
    IF EXISTS(SELECT 1 FROM stock s WHERE (s.pharmacy_id, s.trademark_id) = (_pharmacy_id, _trademark_id)) THEN
        amount := (SELECT availability a
                   FROM stock s
                   WHERE ((s.pharmacy_id, s.trademark_id) = (_pharmacy_id, _trademark_id)));

        UPDATE stock s
        SET availability = amount + _availability
        WHERE ((s.pharmacy_id, s.trademark_id) = (_pharmacy_id, _trademark_id));
    ELSIF _availability < 0 THEN
        RAISE NOTICE 'Can''t buy ! This trademark doesn''t exist at this pharmacy';
    ELSE
        INSERT INTO stock(pharmacy_id, trademark_id, availability)
        VALUES (_pharmacy_id, _trademark_id, _availability);
    END If;
    RETURN (SELECT availability FROM stock s WHERE ((s.pharmacy_id, s.trademark_id) = (_pharmacy_id, _trademark_id)));
EXCEPTION
    WHEN check_violation THEN
        RAISE NOTICE 'Can''t buy such an amount! Bought just %', amount;
        UPDATE stock s
        SET availability = 0
        WHERE ((s.pharmacy_id, s.trademark_id) = (_pharmacy_id, _trademark_id));
        RETURN (SELECT availability
                FROM stock s
                WHERE ((s.pharmacy_id, s.trademark_id) = (_pharmacy_id, _trademark_id)));
END;
$$ LANGUAGE plpgsql VOLATILE;

-- USAGE:
-- В случае, если айди не существует, валится ошибка!))))
-- SELECT add_to_stock(2, 100, -1118);
-- SELECT add_to_stock(2, 1, 118);

-----------------------------------------------------------


-- A function takes the companies_id, drugs_id, the name of the new drug,
-- doze, release_price, the distribution method,
-- creates a patent for it and the trademark itself.
-- CREATE TYPE patent_distribution AS ENUM ('free-to-use', 'usage with some constraints', 'restricted-to-use');

DROP FUNCTION IF EXISTS add_trademark(_company_id int, _drug_id int, _name VARCHAR(80), _doze VARCHAR(80), _release_price VARCHAR(80), _distribution VARCHAR(80));

CREATE OR REPLACE FUNCTION add_trademark(_company_id int,
                                         _drug_id int,
                                         _name VARCHAR(80),
                                         _doze DECIMAL,
                                         _release_price DECIMAL,
                                         _distribution VARCHAR(80))
    RETURNS int AS
$$
DECLARE
    _patent_id INTEGER := (SELECT MAX(id) + 1
                           FROM patents);
BEGIN
    INSERT INTO patents(id, distribution) VALUES (_patent_id, _distribution::patent_distribution);
--
    INSERT INTO trademarks(name, doze, release_price, drug_id, company_id, patent_id)
    VALUES (_name, _doze, _release_price, _drug_id, _company_id, _patent_id);
--
    RETURN _patent_id;
END;
$$ LANGUAGE plpgsql VOLATILE;

-- USAGE EXAMPLES
-- SELECT * FROM add_trademark(2,
--     3,
--     'Anamorgggen',
--     0.5,
--     500.6,
--     'usage with some constraints');


drop index if exists trademark_price;

drop index if exists d_mort;
drop index if exists p_mort;

drop index if exists c_mc;
drop index if exists c_npmpa;
drop index if exists c_mc_npmpa;

drop index if exists disease_name;
drop index if exists company_name;
drop index if exists trademark_name;
drop index if exists drug_substance;
drop index if exists poison_substance;
drop index if exists ethnoscience_name;
drop index if exists pharmacy_name;

------------------

-- EXPLAIN ANALYZE
-- SELECT name, doze, release_price
-- FROM trademarks
-- ORDER BY release_price;

create index trademark_price on trademarks using btree (release_price);

------------------

-- EXPLAIN ANALYZE
-- SELECT name
-- FROM diseases
-- ORDER BY mortality DESC;

create index d_mort on diseases using btree (mortality DESC);
---
create index p_mort on poisons using btree (mortality DESC);

------------------
-- INEFFECTIVE OPTIMIZATION --

-- EXPLAIN ANALYZE
-- SELECT *
-- FROM companies
-- ORDER BY market_cap DESC;
--
-- EXPLAIN ANALYZE
-- SELECT *
-- FROM companies
-- ORDER BY market_cap DESC, net_profit_margin_pct_annual DESC;
--
-- create index c_mc on companies using btree (market_cap DESC);
-- create index c_npmpa on companies using btree (net_profit_margin_pct_annual DESC);
-- create index c_mc_npmpa on companies using btree (net_profit_margin_pct_annual DESC, market_cap DESC);
--


------------------



drop index if exists disease_name;

-- DISCARD ALL;
--
-- EXPLAIN ANALYZE
-- SELECT *
-- FROM diseases
-- WHERE name = 'Newton''s kishg';


create index disease_name on diseases using hash (name);


-- EXPLAIN ANALYZE
-- SELECT *
-- FROM diseases
-- WHERE name = 'Newton''s kishg';

-- create index disease_name on diseases using hash (name);
---
create index company_name on companies using hash (name);
create index trademark_name on trademarks using hash (name);
create index drug_substance on drugs using hash (active_substance);
create index poison_substance on poisons using hash (active_substance);
create index ethnoscience_name on ethnoscience using hash (name);
create index pharmacy_name on pharmacies using hash (name);

------------------



