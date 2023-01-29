package ru.viklover.oopgame.api.forms;

import lombok.Data;

@Data
public class RegisterForm {
    public String username;
    public String password;
    public String repeatedPassword;
}