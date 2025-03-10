-- Sample Pi-themed challenges for the Pi Day application

-- ChallengeModel 1: Basic Pi Value
INSERT INTO challenges (title, description, difficulty, sequence_number, active)
VALUES (
           'The Basics of Pi',
           'What is the approximate value of π (Pi) to two decimal places?',
           1,
           1,
           true
       );

SET @challenge1_id = LAST_INSERT_ID();

INSERT INTO answer_options (challenge_id, option_text, is_correct, option_order)
VALUES
    (@challenge1_id, '3.14', true, 0),
    (@challenge1_id, '3.41', false, 1),
    (@challenge1_id, '3.16', false, 2),
    (@challenge1_id, '3.24', false, 3);

-- ChallengeModel 2: Pi Day
INSERT INTO challenges (title, description, difficulty, sequence_number, active)
VALUES (
           'Pi Day Celebration',
           'Why is Pi Day celebrated on March 14th?',
           1,
           2,
           true
       );

SET @challenge2_id = LAST_INSERT_ID();

INSERT INTO answer_options (challenge_id, option_text, is_correct, option_order)
VALUES
    (@challenge2_id, 'Because 3/14 matches the first digits of Pi (3.14)', true, 0),
    (@challenge2_id, 'Because it was the day Pi was discovered', false, 1),
    (@challenge2_id, 'Because it is Albert Einstein\'s birthday', false, 2),
    (@challenge2_id, 'Because March is Mathematics Month', false, 3);

-- ChallengeModel 3: Circle Area
INSERT INTO challenges (title, description, difficulty, sequence_number, active)
VALUES (
    'Circle Area Challenge',
    'Calculate the area of a circle with radius 3 units. Use π = 3.14 for your calculation.',
    2,
    3,
    true
);

SET @challenge3_id = LAST_INSERT_ID();

INSERT INTO answer_options (challenge_id, option_text, is_correct, option_order)
VALUES
    (@challenge3_id, '28.26 square units', true, 0),
    (@challenge3_id, '18.84 square units', false, 1),
    (@challenge3_id, '9.42 square units', false, 2),
    (@challenge3_id, '56.52 square units', false, 3);

-- ChallengeModel 4: Circle Circumference
INSERT INTO challenges (title, description, difficulty, sequence_number, active)
VALUES (
    'Circle Circumference',
    'Calculate the circumference of a circle with diameter 7 units. Use π = 3.14 for your calculation.',
    2,
    4,
    true
);

SET @challenge4_id = LAST_INSERT_ID();

INSERT INTO answer_options (challenge_id, option_text, is_correct, option_order)
VALUES
    (@challenge4_id, '21.98 units', true, 0),
    (@challenge4_id, '10.99 units', false, 1),
    (@challenge4_id, '43.96 units', false, 2),
    (@challenge4_id, '15.7 units', false, 3);

-- ChallengeModel 5: Pi History
INSERT INTO challenges (title, description, difficulty, sequence_number, active)
VALUES (
    'Pi in History',
    'Which ancient civilization approximated Pi as 256/81 (approximately 3.16)?',
    3,
    5,
    true
);

SET @challenge5_id = LAST_INSERT_ID();

INSERT INTO answer_options (challenge_id, option_text, is_correct, option_order)
VALUES
    (@challenge5_id, 'Ancient Egyptians', true, 0),
    (@challenge5_id, 'Babylonians', false, 1),
    (@challenge5_id, 'Ancient Greeks', false, 2),
    (@challenge5_id, 'Ancient Chinese', false, 3);

-- ChallengeModel 6: Pi Digits
INSERT INTO challenges (title, description, difficulty, sequence_number, active)
VALUES (
    'Pi Digits',
    'What are the next three digits of Pi after 3.14159?',
    3,
    6,
    true
);

SET @challenge6_id = LAST_INSERT_ID();

INSERT INTO answer_options (challenge_id, option_text, is_correct, option_order)
VALUES
    (@challenge6_id, '265', true, 0),
    (@challenge6_id, '256', false, 1),
    (@challenge6_id, '653', false, 2),
    (@challenge6_id, '358', false, 3);

-- ChallengeModel 7: Pi in Sciences
INSERT INTO challenges (title, description, difficulty, sequence_number, active)
VALUES (
    'Pi in Sciences',
    'In which of these formulas is Pi NOT used?',
    4,
    7,
    true
);

SET @challenge7_id = LAST_INSERT_ID();

INSERT INTO answer_options (challenge_id, option_text, is_correct, option_order)
VALUES
    (@challenge7_id, 'E = mc²', true, 0),
    (@challenge7_id, 'Circumference of a circle: C = 2πr', false, 1),
    (@challenge7_id, 'Surface area of a sphere: A = 4πr²', false, 2),
    (@challenge7_id, 'Heisenberg\'s uncertainty principle: ΔxΔp ≥ ħ/2', false, 3);

-- ChallengeModel 8: Volume Calculation
INSERT INTO challenges (title, description, difficulty, sequence_number, active)
VALUES (
           'Volume Calculation',
           'Calculate the volume of a cylinder with radius 2 units and height 5 units. Use π = 3.14.',
           3,
           8,
           true
       );

SET @challenge8_id = LAST_INSERT_ID();

INSERT INTO answer_options (challenge_id, option_text, is_correct, option_order)
VALUES
    (@challenge8_id, '62.8 cubic units', true, 0),
    (@challenge8_id, '31.4 cubic units', false, 1),
    (@challenge8_id, '125.6 cubic units', false, 2),
    (@challenge8_id, '12.56 cubic units', false, 3);

-- ChallengeModel 9: Pi Approximation
INSERT INTO challenges (title, description, difficulty, sequence_number, active)
VALUES (
           'Pi Approximation',
           'Which fraction best approximates Pi?',
           4,
           9,
           true
       );

SET @challenge9_id = LAST_INSERT_ID();

INSERT INTO answer_options (challenge_id, option_text, is_correct, option_order)
VALUES
    (@challenge9_id, '22/7', true, 0),
    (@challenge9_id, '3/1', false, 1),
    (@challenge9_id, '333/106', false, 2),
    (@challenge9_id, '7/22', false, 3);

-- ChallengeModel 10: Pi Record
INSERT INTO challenges (title, description, difficulty, sequence_number, active)
VALUES (
           'Pi Record',
           'As of 2023, approximately how many digits of Pi have been calculated?',
           5,
           10,
           true
       );

SET @challenge10_id = LAST_INSERT_ID();

INSERT INTO answer_options (challenge_id, option_text, is_correct, option_order)
VALUES
    (@challenge10_id, 'Over 100 trillion digits', true, 0),
    (@challenge10_id, 'Around 1 million digits', false, 1),
    (@challenge10_id, 'About 1 billion digits', false, 2),
    (@challenge10_id, 'Nearly 10 billion digits', false, 3);