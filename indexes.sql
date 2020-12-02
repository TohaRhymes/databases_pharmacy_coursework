EXPLAIN ANALYZE
SELECT name, doze, release_price
FROM trademarks
ORDER BY release_price;

create index trademark_price on trademarks using btree (release_price);
-- drop index trademark_price;

------------------

EXPLAIN ANALYZE
SELECT name
FROM diseases
ORDER BY mortality DESC;

create index d_mort on diseases using btree (mortality DESC);
-- drop index d_mort;
create index p_mort on poisons using btree (mortality DESC);

------------------

EXPLAIN ANALYZE
SELECT *
FROM diseases
WHERE name = 'Newton''s kishg';

create index d_name on diseases using hash (name);
drop index d_name;