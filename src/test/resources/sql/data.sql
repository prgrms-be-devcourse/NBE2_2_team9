-- Example Data for caregiver_image
INSERT INTO caregiver_image (created_at, modified_at, url)
VALUES ('2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000', 'https://example.com/caregiver1.jpg'),
       ('2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000', 'https://example.com/caregiver2.jpg');

-- Example Data for caregiver
INSERT INTO caregiver (created_at, image_id, modified_at, login_id, password, username, email)
VALUES ('2024-09-25 15:00:00.000000', 1, '2024-09-25 15:00:00.000000', 'caregiver1', 'password1', 'Caregiver One','caregiver1@example.com'),
       ('2024-09-25 15:10:00.000000', 2, '2024-09-25 15:10:00.000000', 'caregiver2', 'password2', 'Caregiver Two','caregiver2@example.com');

-- Example Data for caretaker_image
INSERT INTO caretaker_image (created_at, modified_at, url)
VALUES ('2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000', 'https://example.com/caretaker1.jpg'),
       ('2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000', 'https://example.com/caretaker2.jpg');

-- Example Data for caretaker
INSERT INTO caretaker (created_at, image_id, modified_at, login_id, password, username, email)
VALUES ('2024-09-25 15:00:00.000000', 1, '2024-09-25 15:00:00.000000', 'caretaker1', 'password1', 'Caretaker One','caretaker1@example.com'),
       ('2024-09-25 15:10:00.000000', 2, '2024-09-25 15:10:00.000000', 'caretaker2', 'password2', 'Caretaker Two','caretaker2@example.com');

-- Example Data for caretaker_caregiver relationship
INSERT INTO caretaker_caregiver (caregiver_id, caretaker_id, created_at, modified_at)
VALUES (1, 1, '2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000'),
       (2, 2, '2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000');

-- Example Data for user_medication
INSERT INTO user_medication (dosage, stock, caretaker_id, created_at, end_date, expiration_date, modified_at,start_date, name, description, frequency, type)
VALUES (2, 50, 1, '2024-09-25 15:00:00.000000', '2024-10-01 15:00:00.000000', '2024-12-01 15:00:00.000000',
        '2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000', 'Aspirin', 'Pain reliever', 'ONCE_A_DAY',
        'MEDICATION'),
       (1, 100, 2, '2024-09-25 15:10:00.000000', '2024-10-01 15:10:00.000000', '2024-12-01 15:10:00.000000',
        '2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000', 'Vitamin D', 'Bone health supplement',
        'TWICE_A_DAY', 'SUPPLEMENT');

-- Example Data for notification
INSERT INTO notification (caretaker_id, created_at, modified_at, notification_time, user_medication_id)
VALUES (1, '2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000', '2024-09-26 09:00:00.000000', 1),
       (2, '2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000', '2024-09-26 09:30:00.000000', 2);

-- Example Data for record
INSERT INTO record (created_at, date, modified_at, user_medication_id, taken)
VALUES ('2024-09-25 15:00:00.000000', '2024-09-25 09:00:00.000000', '2024-09-25 15:00:00.000000', 1, 'TAKEN'),
       ('2024-09-25 15:10:00.000000', '2024-09-25 09:30:00.000000', '2024-09-25 15:10:00.000000', 2, 'UNTAKEN');