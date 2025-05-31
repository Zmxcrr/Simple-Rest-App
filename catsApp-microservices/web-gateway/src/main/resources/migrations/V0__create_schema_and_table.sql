CREATE SCHEMA IF NOT EXISTS catsapp_users;

CREATE TABLE IF NOT EXISTS catsapp_users.users (
    username        varchar(255)    NOT NULL,
    password        varchar(255),
    role            varchar(255),
    owner_id        bigint,
    PRIMARY KEY (username),
    CONSTRAINT users_role_check
    CHECK (role::text = ANY
    (ARRAY
    [
           'USER'::character varying,
           'ADMIN'::character varying
    ]::text[])
    )
);