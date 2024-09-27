-- image
INSERT INTO image (created_at, modified_at, url)
VALUES ('2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000', 'https://example.com/caregiver1.jpg'),
       ('2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000', 'https://example.com/caregiver2.jpg'),
       ('2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000', 'https://example.com/caretaker1.jpg'),
       ('2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000', 'https://example.com/caretaker2.jpg');

-- caregiver
INSERT INTO caregiver (login_id, password, username, email, phone_number, role, image_id, created_at, modified_at)
VALUES ('caregiver1', 'password1', 'Caregiver One', 'caregiver1@example.com', '010-1234-5678', 'USER', 1, '2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000'),
       ('caregiver2', 'password2', 'Caregiver Two', 'caregiver2@example.com', '010-2345-6789', 'USER', 2, '2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000');

-- caretaker
INSERT INTO caretaker (login_id, password, username, email, phone_number, role, image_id, created_at, modified_at)
VALUES ('caretaker1', 'password1', 'Caretaker One', 'caretaker1@example.com', '010-3456-7890', 'USER', 3, '2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000'),
       ('caretaker2', 'password2', 'Caretaker Two', 'caretaker2@example.com', '010-4567-8901', 'USER', 4, '2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000');

-- caretaker_caregiver
INSERT INTO caretaker_caregiver (caregiver_id, caretaker_id, created_at, modified_at)
VALUES (1, 1, '2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000'),
       (2, 2, '2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000');

-- user_medication
INSERT INTO user_medication (name, description, dosage, stock, frequency, type, caretaker_id, start_date, end_date, expiration_date, created_at, modified_at)
VALUES ('Aspirin', 'Pain reliever', 2, 50, 'ONCE_A_DAY', 'MEDICATION', 1, '2024-09-25 15:00:00.000000', '2024-10-01 15:00:00.000000', '2024-12-01 15:00:00.000000', '2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000'),
       ('Vitamin D', 'Bone health supplement', 1, 100, 'TWICE_A_DAY', 'SUPPLEMENT', 2, '2024-09-25 15:10:00.000000', '2024-10-01 15:10:00.000000', '2024-12-01 15:10:00.000000', '2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000');

-- notification
INSERT INTO notification (caretaker_id, user_medication_id, notification_time, created_at, modified_at)
VALUES (1, 1, '2024-09-26 09:00:00.000000', '2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000'),
       (2, 2, '2024-09-26 09:30:00.000000', '2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000');

-- record
INSERT INTO record (user_medication_id, taken, date, created_at, modified_at)
VALUES (1, 'TAKEN', '2024-09-25 09:00:00.000000', '2024-09-25 15:00:00.000000', '2024-09-25 15:00:00.000000'),
       (2, 'UNTAKEN', '2024-09-25 09:30:00.000000', '2024-09-25 15:10:00.000000', '2024-09-25 15:10:00.000000');