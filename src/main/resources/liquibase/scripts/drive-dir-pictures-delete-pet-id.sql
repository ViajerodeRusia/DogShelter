-- changeset dsiliukov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: drive_dir_pictures

-- Удаление столбца pet_id из таблицы drive_dir_pictures
ALTER TABLE drive_dir_pictures
    DROP COLUMN pet_id;