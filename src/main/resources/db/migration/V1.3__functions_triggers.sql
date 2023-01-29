
create or replace function default_values()
    returns trigger as $$
begin
    insert into users_roles (user_id, role_id) values (NEW.id, 1);
    return NEW;
end;
$$ language plpgsql;

create trigger default_values after insert on users
    for each row execute procedure default_values();
