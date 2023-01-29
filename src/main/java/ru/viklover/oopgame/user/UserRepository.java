package ru.viklover.oopgame.user;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import ru.viklover.oopgame.api.forms.LoginForm;
import ru.viklover.oopgame.api.forms.RegisterForm;
import ru.viklover.oopgame.user.exception.UserNotFoundException;
import ru.viklover.oopgame.user.role.UserRole;

import java.util.*;

@Repository
@AllArgsConstructor
public class UserRepository {

    public JdbcTemplate jdbcTemplate;

    public boolean checkUserExistingByUsername(String username) {
        return jdbcTemplate.queryForObject(
                "select exists(select * from users where username = ?)",
                Boolean.class, username
        );
    }

    public Optional<User> create(RegisterForm registerForm) {

        if (!registerForm.getPassword().equals(registerForm.getRepeatedPassword()) ||
            checkUserExistingByUsername(registerForm.getUsername()))
            return Optional.empty();

        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            var statement = connection.prepareStatement(
                    "insert into users (username, password_hash) values (?, ?)",
                    new String[]{"id"}
            );

            var preparedStatementSetter = new ArgumentPreparedStatementSetter(new Object[]{
                    registerForm.getUsername(), BCrypt.hashpw(registerForm.getPassword(), BCrypt.gensalt())
            });
            preparedStatementSetter.setValues(statement);

            return statement;

        }, keyHolder);

        return findById((Long) keyHolder.getKey());
    }

    public Optional<User> findById(Long id) {

        var listOfMaps = jdbcTemplate.queryForList("select * from users where id = ?", id);

        if (listOfMaps.isEmpty())
            throw new UserNotFoundException(id);

        var userMap = listOfMaps.get(0);

        var user = new User();
        user.setId((Long) userMap.get("id"));
        user.setUsername((String) userMap.get("username"));
        user.setPasswordHash((String) userMap.get("password_hash"));
        user.setRoles(findUserRolesById(id));

        return Optional.of(user);
    }

    public Optional<User> findByUsername(String username) {

        var listOfMaps = jdbcTemplate.queryForList("select * from users where username = ?", username);

        var userMap = listOfMaps.get(0);

        var user = new User();
        user.setId((Long) userMap.get("id"));
        user.setUsername((String) userMap.get("username"));
        user.setPasswordHash((String) userMap.get("password_hash"));
        user.setRoles(findUserRolesById((Long) userMap.get("id")));

        return Optional.of(user);
    }

    public boolean validatePassword(LoginForm loginForm) {

        var userOptional = findByUsername(loginForm.getUsername());

        if (userOptional.isEmpty())
            throw new UserNotFoundException(1L);

        return BCrypt.checkpw(loginForm.getPassword(), userOptional.get().getPasswordHash());
    }

    public Set<UserRole> findUserRolesById(Long id) {

        var listOfRoles = jdbcTemplate.queryForList(
                "select r.id, r.name from users_roles u inner join roles r on r.id = u.role_id where u.user_id = ?",
                id
        );

        var hashSet = new HashSet<UserRole>();
        listOfRoles.forEach(map -> hashSet.add(new UserRole((Long) map.get("id"), (String) map.get("name"))));

        return hashSet;
    }
}