--liquibase formatted sql

--changeset nevinture:4
create table if not exists user_secret_key (
    id bigint generated always as identity primary key,
    user_id bigint references "user"(id) on delete cascade,
    secret text not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
)