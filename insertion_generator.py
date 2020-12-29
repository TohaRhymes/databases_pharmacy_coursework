from random import randint
import random
import string


def get_random_string(a, b):
    length = randint(a, b)
    letters = string.ascii_lowercase
    result_str = ''.join(random.choice(letters) for i in range(length))
    return result_str


def get_bool():
    return bool(randint(0, 1))


def make_many_to_many(length, first_reps, second_reps):
    cur_set = set()
    for _ in range(length):
        first_id = randint(1, first_reps)
        second_id = randint(1, second_reps)
        while (first_id, second_id) in cur_set:
            first_id = randint(1, first_reps)
            second_id = randint(1, second_reps)
        cur_set.add((first_id, second_id))
    return cur_set


def make_many_to_many_to_many(length, first_reps, second_reps, third_reps):
    cur_set = set()
    third_set = set()
    for _ in range(length):
        f_id = randint(1, first_reps)
        s_id = randint(1, second_reps)
        t_id = randint(1, third_reps)
        while (f_id, s_id, t_id) in cur_set:
            f_id = randint(1, first_reps)
            s_id = randint(1, second_reps)
            t_id = randint(1, third_reps)
        cur_set.add((f_id, s_id, t_id))
        third_set.add(t_id)
    return cur_set


class ArrayRandomGetter:
    def __init__(self, array):
        self.array = array
        self.n = len(array) - 1

    def __next__(self):
        return self.array[randint(0, self.n)]

    def __iter__(self):
        return self


script_name = 'generated_insertion.sql'
script = ''

# ENUM VALUES
pathogen_type_values = ['virus', 'bacterium', 'protozoan', 'prion', 'viroid', 'fungus', 'small animal']
drugs_groups_values = ['Group A (prohibited substances)', 'Group B (limited turnover)', 'Group C (free circulation)']
poison_origin_values = ['nature', 'chemicals', 'synthetic']
development_stage_values = ['Preclinical phase', 'Phase 0', 'Phase I', 'Phase II', 'Phase III', 'Phase IV']
patent_distribution_values = ['free-to-use', 'usage with some constraints', 'restricted-to-use']
# FOR RANDOM VALUES
pathogen_name_endings_values = ['ium', 'us', 'cokk', 'tronus', 'dedekus']
pathogen_action_verb_values = ['harm', 'dissolve', 'oxidize', 'destroy', 'infect', 'parasitize on',
                               'form connections in', 'intoxicate', 'interfere with processes in',
                               'start other processes in', 'violate the functions of', ]
pathogen_action_target_values = ['brains', 'head', 'legs', 'arms', 'brain', 'lips', 'trachea', 'fingernails',
                                 'testicles', 'bladder', 'fallopian tubes', 'ovaries', 'stomach', 'liver', 'spleen',
                                 'lungs', ]

disease_name_values = ['Ott\'\'s', 'Moor\'\'s', 'Alzgeimer\'\'s', 'Sheiduhen\'\'s', 'Toff\'\'s', 'Glottos\'\'s',
                       'Newton\'\'s',
                       "Morgen\'\'s", "Davydov\'\'s"]

poison_name_endings_values = ['um', 'ava', 'um', 'ava', 'um', 'ava', ' by T']

ethnoscience_verb_values = ['meditate on', 'sniff', 'eat', 'drink', 'break', 'cry on', 'look at', 'marry on']
ethnoscience_noun_values = ['potato', 'cards', 'tomato', 'stone', 'redlight', 'fire', 'paper', 'dirt']
ethnoscience_origin_values = ['granny', 'daddy', 'black magic', 'white magic', 'books', 'witcher', 'Yaga', 'Leshiy']

active_substance_ends_values = ['ea', 'on', 'ol', 'rol', 'off', 'ium', 'to', 'nat', 'an', 'id']
active_substance_beginnings_values = ['pheno', 'buto', 'alpha-', 'keno', 'amo', 'poly', 'oxy']

company_specialization_values = ['sequencing', 'viruses', 'retroviruses', 'alcohol', 'synthetic', 'chemicals',
                                 'natural',
                                 'vitamins']
company_names_values = ['silico', 'vitro', 'hemo', 'rhoto', 'ex', 'isk']

pharmacies_name_values = ['Stolichki', 'Nevis', "P and G", "S and F", "W#M", "Ozerki", "Rechki"]

trade_name_start_values = ["Mik", 'LEU', "Com", "Ros", "Hetero", "Anti"]
trade_name_end_values = ["ous", 'preus', "vit", 'uci', 'stormo']

# settings - amount of rows
pathogen_reps = 400
disease_reps = 300
poison_reps = 100
ethno_reps = 150
drugs_reps = 350
d_t_p = 238
d_t_d = 239
e_t_d = 240
company_reps = 228
c_info_reps = 22
dev_reps = 270
patent_reps = 1500
pharm_reps = 700
tradem_reps = 300
stock_rep = 500
# check M-M connections, to be sure:
d_t_p = min(d_t_p, max(int(drugs_reps * poison_reps / 4), 1))
d_t_d = min(d_t_d, max(int(drugs_reps * disease_reps / 4), 1))
e_t_d = min(e_t_d, max(int(ethno_reps * disease_reps / 4), 1))
dev_reps = min(dev_reps, max(int(company_reps * pathogen_reps / 4), 1))
tradem_reps = min(tradem_reps, max(int(drugs_reps * patent_reps * company_reps / 8), 1))
stock_rep = min(stock_rep, max(int(pharm_reps * tradem_reps / 4), 1))

# PATHOGENS
pathogen_name_endings = ArrayRandomGetter(pathogen_name_endings_values)
pathogen_type = ArrayRandomGetter(pathogen_type_values)
pathogen_action_verb = ArrayRandomGetter(pathogen_action_verb_values)
pathogen_action_target = ArrayRandomGetter(pathogen_action_target_values)
for i in range(pathogen_reps):
    name = get_random_string(3, 10) + next(pathogen_name_endings) + ' ' + str(randint(0, 100)) + '__' + str(
        randint(0, 10000))
    path_type = next(pathogen_type)
    action = next(pathogen_action_verb) + ' ' + next(pathogen_action_target)
    script += f"INSERT INTO pathogens(id, name, type, action) VALUES ({i+1}, '{name}', '{path_type}', '{action}');\n"

# DISEASES
disease_name = ArrayRandomGetter(disease_name_values)
for i in range(disease_reps):
    p_id = randint(1, pathogen_reps)
    name = next(disease_name) + ' ' + get_random_string(3, 6)
    mortal = random.random()
    script += f"INSERT INTO diseases(id, pathogen_id, name, mortality) VALUES ({i+1}, {p_id}, '{name}', {mortal});\n"

# POISONS
poison_origin = ArrayRandomGetter(poison_origin_values)
poison_name_endings = ArrayRandomGetter(poison_name_endings_values)
for i in range(poison_reps):
    active_substance = get_random_string(6, 8) + next(poison_name_endings)
    t_act = next(pathogen_action_verb) + ' ' + next(pathogen_action_target)
    t_or = next(poison_origin)
    mortal = random.random()
    script += f"INSERT INTO poisons(id, active_substance, type_by_action, type_by_origin, mortality) " \
              f"VALUES ({i+1}, '{active_substance}', '{t_act}', '{t_or}', {mortal});\n"

# ETHNOSCIENCE
ethnoscience_verb = ArrayRandomGetter(ethnoscience_verb_values)
ethnoscience_noun = ArrayRandomGetter(ethnoscience_noun_values)
ethnoscience_origin = ArrayRandomGetter(ethnoscience_origin_values)
for i in range(ethno_reps):
    e_act = next(ethnoscience_verb) + ' ' + next(ethnoscience_noun)
    e_or = next(ethnoscience_origin)
    script += f"INSERT INTO ethnoscience(id, name, origin) VALUES ({i+1}, '{e_act}', '{e_or}');\n"

# DRUGS
drugs_groups = ArrayRandomGetter(drugs_groups_values)
active_substance_ends = ArrayRandomGetter(active_substance_ends_values)
active_substance_beginnings = ArrayRandomGetter(active_substance_beginnings_values)
for i in range(drugs_reps):
    act_subs = next(active_substance_beginnings) + get_random_string(0, 5) + get_random_string(1, 3) + next(
        active_substance_ends)
    homeo = get_bool()
    dr_group = next(drugs_groups)
    script += f"INSERT INTO drugs(id, active_substance, homeopathy, drugs_group) VALUES ({i+1},'{act_subs}', '{homeo}', '{dr_group}');\n"

# DRUGS_TO_POISONS
# DRUGS_TO_DISEASE
# ETHNOSCIENCE_TO_DISEASE
dtp_set = make_many_to_many(d_t_p, drugs_reps, poison_reps)
for i, (dr, po) in enumerate(dtp_set):
    script += f"INSERT INTO drugs_to_poisons(id, drugs_id, poison_id) VALUES ({i+1}, {dr}, {po});\n"

dtd_set = make_many_to_many(d_t_d, drugs_reps, disease_reps)
for i, (dr, di) in enumerate(dtd_set):
    script += f"INSERT INTO drugs_to_diseases(id, drugs_id, disease_id) VALUES ({i+1}, {dr}, {di});\n"

etd_set = make_many_to_many(e_t_d, ethno_reps, disease_reps)
for i, (et, di) in enumerate(etd_set):
    script += f"INSERT INTO ethnoscience_to_diseases(id, ethnoscience_id, disease_id) VALUES ({i+1}, {et}, {di});\n"

# # COMPANIES & COMPANY_INFO (history)
company_names = ArrayRandomGetter(company_names_values)
company_specialization = ArrayRandomGetter(company_specialization_values)
for i in range(company_reps):
    com_name = get_random_string(4, 8) + next(company_names)
    specialization = next(company_specialization) + ', ' + next(company_specialization)
    script += f"INSERT INTO companies(id, name, specialization) VALUES ({i+1},'{com_name}', '{specialization}');\n"
    for _ in range(randint(0, c_info_reps)):
        script += f"UPDATE companies SET (market_cap, net_profit_margin_pct_annual) = ({randint(0, 100000)}, {randint(0, 100000)}) where name='{com_name}';\n"

# DEVELOPMENT
development_stage = ArrayRandomGetter(development_stage_values)
dev_set = make_many_to_many(dev_reps, company_reps, pathogen_reps)
for i, (co, pa) in enumerate(dev_set):
    stage = next(development_stage)
    failed = get_bool()
    script += f"INSERT INTO development(id, company_id,pathogen_id, testing_stage, failed) VALUES ({i+1}, {co}, {pa}, '{stage}', '{failed}');\n"

# PATENTS
patent_distribution = ArrayRandomGetter(patent_distribution_values)
for i in range(patent_reps):
    distr = next(patent_distribution)
    start_date = str(randint(1900, 2021)) + '-' + str(randint(1, 12)) + '-' + str(randint(1, 28))
    script += f"INSERT INTO patents(id, distribution, start_date) VALUES ({i+1},'{distr}', '{start_date}');\n"

# PHARMACIES
pharmacies_name = ArrayRandomGetter(pharmacies_name_values)
for i in range(pharm_reps):
    name = next(pharmacies_name) + ' in ' + next(company_names)
    price_mul = randint(100, 500) / 100
    price_plus = randint(0, 5000) / 100
    script += f"INSERT INTO pharmacies(id, name, price_mul, price_plus) VALUES ({i+1},'{name}', {price_mul}, {price_plus});\n"

# TRADEMARKS
trade_name_start = ArrayRandomGetter(trade_name_start_values)
trade_name_end = ArrayRandomGetter(trade_name_end_values)
trade_set = make_many_to_many_to_many(tradem_reps, drugs_reps, company_reps, patent_reps)
for i, (a, b, c) in enumerate(trade_set):
    name = next(trade_name_start) + get_random_string(5, 9) + next(trade_name_end)
    doze = randint(1, 1000)
    rel_price = randint(20, 1000)
    script += f"INSERT INTO trademarks(id, name, doze, release_price, drug_id, company_id, patent_id) VALUES({i+1},'{name}', {doze}, {rel_price}, {a}, {b}, {c});\n"

# STOCK
stock_set = make_many_to_many(stock_rep, pharm_reps, tradem_reps)
for i, (a, b) in enumerate(stock_set):
    if random.random() > 0.7:
        script += f"INSERT INTO stock(id, pharmacy_id, trademark_id) VALUES ({i+1},{a}, {b});\n"
    else:
        availability = randint(0, 100)
        script += f"INSERT INTO stock(id, pharmacy_id, trademark_id, availability) VALUES ({i+1},{a}, {b}, {availability});\n"

print(script)
with open(script_name, 'w') as ouput_script:
    ouput_script.write(script)
