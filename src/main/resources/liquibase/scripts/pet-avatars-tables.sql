-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: pets

-- Создание таблицы для хранения аватаров питомцев
CREATE TABLE IF NOT EXISTS  pet_avatars (
    id BIGSERIAL PRIMARY KEY,           -- Уникальный идентификатор аватара
    file_path VARCHAR(255) NOT NULL,    -- Путь к файлу аватара
    file_size BIGINT NOT NULL,          -- Размер файла аватара
    media_type VARCHAR(255) NOT NULL,   -- Тип медиа файла аватара
    data BYTEA NOT NULL,                -- Данные файла аватара
    pet_id BIGINT NOT NULL,             -- Идентификатор питомца, ссылка на таблицу pets
    CONSTRAINT fk_pet
        FOREIGN KEY (pet_id)
        REFERENCES pets (id)
        ON DELETE CASCADE
);

-- changeset dsiliukov:${changeset.id.sequence}

ALTER TABLE pet_avatars
    ALTER COLUMN data TYPE oid
    USING lo_from_bytea(0, data); -- Используем функцию lo_from_bytea для преобразования bytea в oid