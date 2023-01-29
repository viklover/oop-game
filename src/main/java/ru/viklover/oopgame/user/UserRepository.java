package ru.viklover.oopgame.user;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@AllArgsConstructor
public class UserRepository {

    public JdbcTemplate jdbcTemplate;

    public User create(String username, String password) {

        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            var statement = connection.prepareStatement(
                    "insert into users (username, password_hash) values (?, ?)",
                    new String[]{"id"}
            );

            var preparedStatementSetter = new ArgumentPreparedStatementSetter(new Object[]{
                    username, BCrypt.hashpw(password, BCrypt.gensalt())
            });
            preparedStatementSetter.setValues(statement);

            return statement;

        }, keyHolder);

        return findById((Long) keyHolder.getKey());
    }

    public User findById(Long id) {

        var userMap = jdbcTemplate.queryForMap("select * from users where id = ?", id);

        var user = new User();
        user.setId((Long) userMap.get("id"));
        user.setUsername((String) userMap.get("username"));
        user.setPasswordHash((String) userMap.get("password_hash"));
        user.setRoles(findUserRolesById(id));

        return user;
    }

    public User findByUsername(String username) {

        var userMap = jdbcTemplate.queryForMap("select * from users where username = ?", username);

        var user = new User();
        user.setId((Long) userMap.get("id"));
        user.setUsername((String) userMap.get("username"));
        user.setPasswordHash((String) userMap.get("password_hash"));
        user.setRoles(findUserRolesById((Long) userMap.get("id")));

        return user;
    }

    public Set<UserRole> findUserRolesById(Long id) {

        var listOfRoles = jdbcTemplate.queryForList(
                "select r.id as id, r.name as name from users_roles u inner join roles r on r.id = u.role_id where u.user_id = ?",
                id
        );

        var hashSet = new HashSet<UserRole>();
        listOfRoles.forEach(map ->
            hashSet.add(new UserRole((Integer) map.get("id"), (String) map.get("name")))
        );

        return hashSet;
    }


    public boolean checkExistingById(Long id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "select exists(select * from users where id = ?)",
                Boolean.class, id
        ));
    }

    public boolean checkExistingByUsername(String username) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                "select exists(select * from users where username = ?)",
                Boolean.class, username
        ));
    }
}