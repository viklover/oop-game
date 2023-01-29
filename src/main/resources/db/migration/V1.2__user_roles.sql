create table roles
(
    id   serial primary key,
    name text unique not null
);

create table users_roles
(
    user_id bigserial not null,
    role_id serial    not null,
    unique (user_id, role_id),
    foreign key (user_id) references users (id) on delete cascade,
    foreign key (role_id) references roles (id) on delete cascade
);

insert into roles (name) values ('user'), ('admin');