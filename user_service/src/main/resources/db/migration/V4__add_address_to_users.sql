ALTER TABLE users
    ADD COLUMN address_street       VARCHAR(255) NOT NULL DEFAULT '',
    ADD COLUMN address_number       VARCHAR(20)  NOT NULL DEFAULT '',
    ADD COLUMN address_complement   VARCHAR(100),
    ADD COLUMN address_neighborhood VARCHAR(100) NOT NULL DEFAULT '',
    ADD COLUMN address_city         VARCHAR(100) NOT NULL DEFAULT '',
    ADD COLUMN address_state        CHAR(2)      NOT NULL DEFAULT '',
    ADD COLUMN address_zip_code     CHAR(8)      NOT NULL DEFAULT '';

ALTER TABLE users
    ALTER COLUMN address_street       DROP DEFAULT,
    ALTER COLUMN address_number       DROP DEFAULT,
    ALTER COLUMN address_neighborhood DROP DEFAULT,
    ALTER COLUMN address_city         DROP DEFAULT,
    ALTER COLUMN address_state        DROP DEFAULT,
    ALTER COLUMN address_zip_code     DROP DEFAULT;
