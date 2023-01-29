package ru.viklover.oopgame.api.auth.form;

import lombok.Data;

@Data
public class RegisterForm {
    public String username;
    public String password;
    public String repeatedPassword;
}