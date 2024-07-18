-- liquibase formatted sql

-- changeset mmaksimov:${changeset.id.sequence}

-- Создание таблицы для хранения информации о директории
CREATE TABLE IF NOT EXISTS  drive_dir_pictures (
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

ALTER TABLE drive_dir_pictures
    ALTER COLUMN data TYPE oid
    USING lo_from_bytea(0, data); -- Используем функцию lo_from_bytea для преобразования bytea в oid

-- changeset dsiliukov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: shelters

-- Добавление столбца shelter_id для связи с таблицей shelters
ALTER TABLE drive_dir_pictures
    ADD COLUMN shelter_id BIGINT,
    ADD CONSTRAINT fk_shelter_id
        FOREIGN KEY (shelter_id)
        REFERENCES shelters (id);