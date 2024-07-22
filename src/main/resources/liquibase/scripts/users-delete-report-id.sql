-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: users

ALTER TABLE users
DROP COLUMN report_id;