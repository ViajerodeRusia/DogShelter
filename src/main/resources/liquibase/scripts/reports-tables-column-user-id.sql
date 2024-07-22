-- liquibase formatted sql

-- changeset dkhan:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: users

-- Добавление столбца user_id в таблицу reports
ALTER TABLE reports ADD COLUMN user_id BIGINT;

-- changeset dkhan:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: users

-- Добавление внешнего ключа к столбцу user_id
ALTER TABLE reports ADD CONSTRAINT fk_user
FOREIGN KEY (user_id) REFERENCES users(id);