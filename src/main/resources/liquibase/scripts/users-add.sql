-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

ALTER TABLE users
ADD COLUMN extension BOOLEAN DEFAULT FALSE NOT NULL;