--liquibase formatted sql

--changeset nevinture:1
create table if not exists location_snapshot(
    id bigint generated always as identity primary key,
    origin_id bigint references location(id) on delete cascade,
    slug varchar(200) unique not null,
    name varchar(200),
    language varchar(20),
    change_at timestamp with time zone
)