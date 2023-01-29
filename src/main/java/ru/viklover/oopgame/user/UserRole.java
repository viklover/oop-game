package ru.viklover.oopgame.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRole {
    private Integer id;
    private String name;
}