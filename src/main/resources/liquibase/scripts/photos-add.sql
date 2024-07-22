-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

ALTER TABLE Photo ADD COLUMN data BYTEA;