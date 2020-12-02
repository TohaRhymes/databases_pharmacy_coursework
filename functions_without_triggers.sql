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

DROP FUNCTION IF EXISTS add_trademark(_company_id int, _drug_id int, _name VARCHAR(80), _doze VARCHAR(80), _release_price VARCHAR(80), _distribution  VARCHAR(80));

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


