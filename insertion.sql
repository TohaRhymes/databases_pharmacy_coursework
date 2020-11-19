INSERT INTO pathogens(id, name, type, action)
VALUES (1, 'cov2019', 'virus', 'infect lungs');
INSERT INTO pathogens(id, name, type, action)
VALUES (2, 'Treponema pallidum', 'bacterium', 'violate skeen');

INSERT INTO diseases(id, pathogen_id, name, mortality)
VALUES (1, 1, 'loss of smell', 0.001);
INSERT INTO diseases(id, pathogen_id, name, mortality)
VALUES (2, 1, 'coma', 0.95);
INSERT INTO diseases(id, pathogen_id, name, mortality)
VALUES (3, 2, 'Syphilis', 0.23);

INSERT INTO poisons(id, active_substance, type_by_action, type_by_origin, mortality)
VALUES (1, 'Arsenicum', 'intoxicate brains', 'synthetic', 0.74);

INSERT INTO ethnoscience(id, name, origin)
VALUES (1, 'подышать над картошкой', 'картофель');
INSERT INTO ethnoscience(id, name, origin)
VALUES (2, 'чай c ромашкой', 'ромашка');
INSERT INTO ethnoscience(id, name, origin)
VALUES (3, 'погадать на картах Таро', 'потусторонние силы');

INSERT INTO drugs(id, active_substance, homeopathy, drugs_group)
VALUES (1, 'Echinacea purpurea D3', true, 'Group C (free circulation)');
INSERT INTO drugs(id, active_substance, homeopathy, drugs_group)
VALUES (2, 'interferon', false, 'Group B (limited turnover)');
INSERT INTO drugs(id, active_substance, homeopathy, drugs_group)
VALUES (3, 'Phenol', false, 'Group A (prohibited substances)');


INSERT INTO drugs_to_poisons
VALUES (1, 1);
INSERT INTO drugs_to_poisons
VALUES (2, 1);

INSERT INTO drugs_to_diseases
VALUES (2, 1);
INSERT INTO drugs_to_diseases
VALUES (3, 2);

INSERT INTO ethnoscience_to_diseases
VALUES (2, 1);
INSERT INTO ethnoscience_to_diseases
VALUES (1, 3);



INSERT INTO companies(id, name, specialization)
VALUES (1, 'RosVitro', 'viruses');
UPDATE companies
SET (market_cap, net_profit_margin_pct_annual) = (10, 100)
where name = 'RosVitro';
UPDATE companies
SET (name, specialization, market_cap, net_profit_margin_pct_annual)= ('RosVitro', 'viruses', 50, 34);

INSERT INTO companies(id, name, specialization, market_cap, net_profit_margin_pct_annual)
VALUES (2, 'Grindex', 'synthetic', 1000, 2130);


INSERT INTO patents(id, distribution, start_date)
VALUES (1, 'free-to-use', '2010-10-10');
INSERT INTO patents(id, distribution)
VALUES (2, 'restricted-to-use');
INSERT INTO patents(id, distribution, start_date)
VALUES (3, 'free-to-use', '2012-12-10');
INSERT INTO patents(id, distribution, start_date)
VALUES (4, 'free-to-use', '2019-05-04');

INSERT INTO pharmacies(id, name, price_mul, price_plus)
VALUES (1, 'Stolichki', 1.05, 3);
INSERT INTO pharmacies(id, name, price_mul, price_plus)
VALUES (2, 'Stolichki-2', 1.15, 3);
INSERT INTO pharmacies(id, name, price_mul, price_plus)
VALUES (3, 'Stolichki-3', 1.0, 15);

INSERT INTO trademarks(id, name, doze, release_price, drug_id, company_id, patent_id)
VALUES (1, 'Anaferon', 3, 158, 2, 1, 1);
INSERT INTO trademarks(id, name, doze, release_price, drug_id, company_id, patent_id)
VALUES (2, 'Anaferon-beta', 3, 358, 2, 2, 4);
INSERT INTO trademarks(id, name, doze, release_price, drug_id, company_id, patent_id)
VALUES (3, 'prostaden', 200, 35, 1, 2, 2);


INSERT INTO stock(id, pharmacy_id, trademark_id, availability)
VALUES (1, 1, 1, 100);
INSERT INTO stock(id, pharmacy_id, trademark_id, availability)
VALUES (2, 1, 1, 2);
INSERT INTO stock(id, pharmacy_id, trademark_id, availability)
VALUES (3, 1, 1, 0);


INSERT INTO development(id, company_id, pathogen_id, testing_stage, failed)
VALUES (1, 1, 2, 'Phase III', true);
INSERT INTO development(id, company_id, pathogen_id, testing_stage, failed)
VALUES (2, 1, 2, 'Preclinical phase', false);
INSERT INTO development(id, company_id, pathogen_id, testing_stage, failed)
VALUES (3, 2, 1, 'Phase IV', false);
