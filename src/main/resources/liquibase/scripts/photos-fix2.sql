-- liquibase formatted sql
-- changeset dsiliukov:${changeset.id.sequence}

ALTER TABLE Photo
    ALTER COLUMN data TYPE oid
    USING lo_from_bytea(0, data); -- Используем функцию lo_from_bytea для преобразования bytea в oid