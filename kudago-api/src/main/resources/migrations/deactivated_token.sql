--liquibase formatted sql

--changeset nevinture:3
create table if not exists deactivated_token(
    id uuid primary key,
    expires_at timestamp with time zone not null
)