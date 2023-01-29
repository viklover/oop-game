package ru.viklover.oopgame.user;

import lombok.Data;

import java.util.Set;

@Data
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private Set<UserRole> roles;
}