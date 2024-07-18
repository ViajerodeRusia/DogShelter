-- liquibase formatted sql

-- changeset mmaksimov:${changeset.id.sequence}

-- Создание таблицы для хранения информации об отчетах
CREATE TABLE IF NOT EXISTS reports (
    id BIGSERIAL PRIMARY KEY,       -- Уникальный идентификатор отчета
    text TEXT NOT NULL,             -- Текст отчета
    date DATE NOT NULL              -- Дата отчета
);

-- changeset mmaksimov:${changeset.id.sequence}

-- Создание колонки check
ALTER TABLE reports
    ADD COLUMN check_report BOOLEAN;       --Статус отчета

