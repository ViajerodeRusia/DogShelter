-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

ALTER TABLE pets
ADD COLUMN date_adoption TIMESTAMP;