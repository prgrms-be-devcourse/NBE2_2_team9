DROP TABLE IF EXISTS record CASCADE;
DROP TABLE IF EXISTS notification CASCADE;
DROP TABLE IF EXISTS user_medication CASCADE;
DROP TABLE IF EXISTS caretaker_caregiver CASCADE;
DROP TABLE IF EXISTS caretaker CASCADE;
DROP TABLE IF EXISTS caregiver CASCADE;
DROP TABLE IF EXISTS image CASCADE;

CREATE TABLE caregiver
(
    caregiver_id BIGINT      NOT NULL AUTO_INCREMENT,
    login_id     VARCHAR(20) NOT NULL,
    password     VARCHAR(30) NOT NULL,
    username     VARCHAR(30) NOT NULL,
    email        VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    role         ENUM ('ADMIN', 'USER') NOT NULL,
    image_id     BIGINT,
    created_at   DATETIME(6) NOT NULL,
    modified_at  DATETIME(6),
    PRIMARY KEY (caregiver_id),
    UNIQUE (login_id),
    UNIQUE (email),
    UNIQUE (image_id),
    UNIQUE (phone_number)
);

CREATE TABLE image
(
    image_id          BIGINT       NOT NULL AUTO_INCREMENT,
    url         VARCHAR(255) NOT NULL,
    created_at   DATETIME(6) NOT NULL,
    modified_at  DATETIME(6),
    PRIMARY KEY (id)
);

CREATE TABLE caretaker
(
    caretaker_id BIGINT      NOT NULL AUTO_INCREMENT,
    login_id     VARCHAR(20) NOT NULL,
    password     VARCHAR(30) NOT NULL,
    username     VARCHAR(30) NOT NULL,
    email        VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    role         ENUM ('ADMIN', 'USER') NOT NULL,
    image_id     BIGINT,
    created_at   DATETIME(6) NOT NULL,
    modified_at  DATETIME(6),
    PRIMARY KEY (caretaker_id),
    UNIQUE (login_id),
    UNIQUE (email),
    UNIQUE (image_id),
    UNIQUE (phone_number)
);

CREATE TABLE caretaker_caregiver
(
    caretaker_caregiver_id BIGINT      NOT NULL AUTO_INCREMENT,
    caregiver_id           BIGINT,
    caretaker_id           BIGINT,
    created_at             DATETIME(6) NOT NULL,
    modified_at            DATETIME(6),
    PRIMARY KEY (caretaker_caregiver_id),
    FOREIGN KEY (caregiver_id) REFERENCES caregiver (caregiver_id),
    FOREIGN KEY (caretaker_id) REFERENCES caretaker (caretaker_id)
);

CREATE TABLE user_medication
(
    user_medication_id BIGINT                                                                                                NOT NULL AUTO_INCREMENT,
    caretaker_id       BIGINT,
    name               VARCHAR(30)                                                                                           NOT NULL,
    description        VARCHAR(100),
    dosage             INT,
    stock              INT                                                                                                   NOT NULL,
    frequency          ENUM ('AS_NEEDED', 'BIWEEKLY', 'MONTHLY', 'ONCE_A_DAY', 'THREE_TIMES_A_DAY', 'TWICE_A_DAY', 'WEEKLY') NOT NULL,
    type               ENUM ('MEDICATION', 'SUPPLEMENT')                                                                     NOT NULL,
    start_date         DATETIME(6)                                                                                           NOT NULL,
    end_date           DATETIME(6)                                                                                           NOT NULL,
    expiration_date    DATETIME(6)                                                                                           NOT NULL,
    created_at         DATETIME(6)                                                                                           NOT NULL,
    modified_at        DATETIME(6),
    PRIMARY KEY (user_medication_id),
    FOREIGN KEY (caretaker_id) REFERENCES caretaker (caretaker_id)
);

CREATE TABLE notification
(
    notification_id    BIGINT      NOT NULL AUTO_INCREMENT,
    caretaker_id       BIGINT,
    user_medication_id BIGINT,
    notification_time  DATETIME(6) NOT NULL,
    created_at         DATETIME(6) NOT NULL,
    modified_at        DATETIME(6),
    PRIMARY KEY (notification_id),
    FOREIGN KEY (caretaker_id) REFERENCES caretaker (caretaker_id),
    FOREIGN KEY (user_medication_id) REFERENCES user_medication (user_medication_id)
);

CREATE TABLE record
(
    record_id          BIGINT                    NOT NULL AUTO_INCREMENT,
    user_medication_id BIGINT,
    taken              ENUM ('TAKEN', 'UNTAKEN') NOT NULL,
    date               DATETIME(6)               NOT NULL,
    created_at         DATETIME(6)               NOT NULL,
    modified_at        DATETIME(6),
    PRIMARY KEY (record_id),
    FOREIGN KEY (user_medication_id) REFERENCES user_medication (user_medication_id)
);