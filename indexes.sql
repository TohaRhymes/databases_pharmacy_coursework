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



