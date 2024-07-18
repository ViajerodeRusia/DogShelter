-- liquibase formatted sql
-- changeset mmaksimov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: users

-- Создание колонки со статусом продления отчетов для пользователя.
ALTER TABLE users
    ADD COLUMN extension BOOLEAN;