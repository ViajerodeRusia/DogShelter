-- liquibase formatted sql

-- changeset mmaksimov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: reports

-- удаление колонки report_id
ALTER TABLE users
    DROP COLUMN report_id;                --Идентификатор отчета, ссылка на таблицу reports. Не нужен в этой таблице.

-- changeset mmaksimov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: reports

-- Создание колонки для связи между пользователями и их отчетами
ALTER TABLE reports
    ADD COLUMN user_id BIGSERIAL,                 --Идентификатор пользователя, ссылка на таблицу users
    ADD CONSTRAINT FK_user_id
        FOREIGN KEY (user_id)
        REFERENCES users(id);