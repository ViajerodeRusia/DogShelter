-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

-- Создание таблицы для хранения информации о питомцах
CREATE TABLE IF NOT EXISTS pets (
    id BIGSERIAL PRIMARY KEY,           -- Уникальный идентификатор питомца
    name VARCHAR(255) NOT NULL,         -- Имя питомца
    breed VARCHAR(255) NOT NULL,        -- Порода питомца
    age INT NOT NULL CHECK (age >= 0),  -- Возраст питомца (не может быть отрицательным)
    food VARCHAR(255)                   -- Предпочитаемая еда питомца
);

-- changeset dsiliukov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: shelters

-- Добавление столбца shelter_id в таблицу pets
ALTER TABLE pets ADD COLUMN shelter_id BIGINT;

-- changeset dsiliukov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: shelters

-- Добавление внешнего ключа к столбцу shelter_id
ALTER TABLE pets ADD CONSTRAINT fk_shelter
FOREIGN KEY (shelter_id) REFERENCES shelters(id);
