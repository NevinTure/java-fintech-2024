--liquibase formatted sql

--changeset nevinture:2
create table if not exists role(
    id bigint generated always as identity primary key,
    name text unique not null,
    description text,
    created_at timestamp with time zone default current_timestamp(0),
    updated_at timestamp with time zone default current_timestamp(0)
);

insert into role(name, description) values ('USER', 'user role'), ('ADMIN', 'admin role') on conflict do nothing;