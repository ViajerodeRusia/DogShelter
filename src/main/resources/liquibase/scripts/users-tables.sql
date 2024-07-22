-- liquibase formatted sql

-- changeset dkhan:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: pets

-- Создание таблицы для хранения информации о пользователях
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,                 -- Уникальный идентификатор пользователя
    chat_id BIGINT UNIQUE NOT NULL,           -- Идентификатор чата в Telegram (обязательное поле, уникальное)
    name VARCHAR(255) NOT NULL,               -- Имя пользователя (обязательное поле)
    phone_number VARCHAR(11) NOT NULL,        -- Номер телефона пользователя (обязательное поле)
    pet_id BIGINT,                            -- Идентификатор питомца, ссылка на таблицу pets
    CONSTRAINT fk_pet_id
        FOREIGN KEY (pet_id)
        REFERENCES pets (id)
);