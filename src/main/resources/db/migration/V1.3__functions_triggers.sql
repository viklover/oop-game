
CREATE OR REPLACE FUNCTION default_values()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO users_roles (user_id, role_id) VALUES (NEW.id, 1);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER default_values AFTER INSERT ON users
    FOR EACH ROW EXECUTE PROCEDURE default_values();
