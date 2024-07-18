-- liquibase formatted sql

-- changeset mmaksimov:${changeset.id.sequence}

-- Создание таблицы для хранения информации о приютах
CREATE TABLE IF NOT EXISTS shelters (
    id BIGSERIAL PRIMARY KEY,           -- Уникальный идентификатор приюта
    name VARCHAR(255) NOT NULL,         -- Имя приюта
    address VARCHAR(255) NOT NULL     -- Адрес приюта
);
