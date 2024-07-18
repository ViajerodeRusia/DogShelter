-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: users

-- Добавление столбца user_id в таблицу pets
ALTER TABLE pets ADD COLUMN user_id BIGINT;

-- changeset dsiliukov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: users

-- Добавление внешнего ключа к столбцу user_id
ALTER TABLE pets ADD CONSTRAINT fk_user
FOREIGN KEY (user_id) REFERENCES users(id);