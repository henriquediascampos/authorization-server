--liquibase formatted sql

--changeset henrique.campos:create_table:users
--comment create table users
--preconditions onFail:MARK_RAN onError:HALT
--precondition-sql-check expectedResult:0 ${tables_exists} = UPPER('users') AND ${checkSchema}
CREATE TABLE user_entity (
    id ${uuid} PRIMARY KEY,
    fullname VARCHAR(256) NOT NULL,
    username VARCHAR(256) NOT NULL UNIQUE,
    email VARCHAR(500) NOT NULL UNIQUE,
    password VARCHAR(1000) NOT NULL,
    enabled ${boolean} default ${true},
    checked_email ${boolean} default ${false},
    deleted ${boolean} default ${false},
    created_at ${zonedDateTime},
    updated_at ${zonedDateTime},
    created_by ${uuid},
    updated_by ${uuid},
    scopes ${varchar}(5000) NOT NULL
);

COMMENT ON COLUMN user_entity.id IS 'register identifier ';
COMMENT ON COLUMN user_entity.username IS 'username login option';
COMMENT ON COLUMN user_entity.email IS 'User email login option';
COMMENT ON COLUMN user_entity.password IS 'user login password';
COMMENT ON COLUMN user_entity.fullname IS 'Users full name';
COMMENT ON COLUMN user_entity.enabled IS 'identifies if the account is enabled';
COMMENT ON COLUMN user_entity.deleted IS 'identifies if the account is deleted';
COMMENT ON COLUMN user_entity.checked_email IS 'identifies if the email has been checked';
COMMENT ON COLUMN user_entity.created_at IS 'registration date';
COMMENT ON COLUMN user_entity.updated_at IS 'update date';
COMMENT ON COLUMN user_entity.created_by IS 'create user fk';
COMMENT ON COLUMN user_entity.updated_by IS 'update user fk';
COMMENT ON COLUMN user_entity.scopes IS 'scopes of user (permitions) (separate by comma: ADMIN,USER)';

