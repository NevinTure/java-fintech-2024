--liquibase formatted sql

--changeset nevinture:2
create table if not exists "user"(
    id bigint generated always as identity primary key,
    name text not null unique,
    email text not null unique,
    salt text not null,
    password text not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null
)