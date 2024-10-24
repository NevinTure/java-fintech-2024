--liquibase formatted sql

--changeset nevinture:1
create table if not exists locations (
    id bigint generated always as identity primary key,
    slug varchar(200) unique not null,
    name varchar(200)
)