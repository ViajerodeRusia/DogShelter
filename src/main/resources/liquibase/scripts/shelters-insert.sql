-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

-- Добавление данных в таблицу shelters
INSERT INTO shelters (name, address) VALUES
('Приют "Дружба"', 'Москва, ул. Пушкина, д. 10'),
('Приют "Четыре лапы"', 'Москва, ул. Лермонтова, д. 5');