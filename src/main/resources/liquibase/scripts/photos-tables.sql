-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

CREATE TABLE Photo (
    id BIGSERIAL PRIMARY KEY,
    fileId VARCHAR(255) NOT NULL,
    filePath VARCHAR(255) NOT NULL,
    report_id BIGINT NOT NULL,          -- Идентификатор отчета, ссылка на таблицу reports
        CONSTRAINT fk_reports
            FOREIGN KEY (report_id)
            REFERENCES reports (id)
            ON DELETE CASCADE
);