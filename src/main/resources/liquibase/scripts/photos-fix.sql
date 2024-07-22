-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

ALTER TABLE Photo
    ALTER COLUMN report_id DROP NOT NULL;