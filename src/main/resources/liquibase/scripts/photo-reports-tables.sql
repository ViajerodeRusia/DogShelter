-- liquibase formatted sql

-- changeset mmaksimov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: reports

-- Создание таблицы для хранения фотографий отчетов
CREATE TABLE IF NOT EXISTS photo_reports (
    id BIGSERIAL PRIMARY KEY,           -- Уникальный идентификатор фото
    file_path VARCHAR(255) NOT NULL,    -- Путь к файлу фото
    file_size BIGINT NOT NULL,          -- Размер файла фото
    media_type VARCHAR(255) NOT NULL,   -- Тип медиа файла фото
    data BYTEA NOT NULL,                -- Данные файла фото
    report_id BIGINT NOT NULL,          -- Идентификатор отчета, ссылка на таблицу reports
    CONSTRAINT fk_reports
        FOREIGN KEY (report_id)
        REFERENCES reports (id)
        ON DELETE CASCADE
);

-- changeset dsiliukov:${changeset.id.sequence}

ALTER TABLE photo_reports
    ALTER COLUMN data TYPE oid
    USING lo_from_bytea(0, data); -- Используем функцию lo_from_bytea для преобразования bytea в oid