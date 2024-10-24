--liquibase formatted sql

--changeset nevinture:1
create table if not exists events (
    id bigint generated always as identity primary key,
    title text,
    slug text unique not null,
    date date,
    location_id bigint references locations(id)
)