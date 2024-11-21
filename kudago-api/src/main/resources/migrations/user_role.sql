--liquibase formatted sql

--changeset nevinture:2
create table if not exists user_role(
    user_id bigint references "user"(id),
    role_id bigint references  role(id),
    primary key (user_id, role_id)
)