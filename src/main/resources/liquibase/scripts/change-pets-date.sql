-- liquibase formatted sql
-- changeset mmaksimov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: pets

-- Создание колонки с датой усыновления для отсчета 30 дней для напоминаний об отчетах
ALTER TABLE pets
    ADD COLUMN date_adoption DATE;