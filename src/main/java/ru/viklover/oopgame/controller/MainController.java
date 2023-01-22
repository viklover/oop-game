package ru.viklover.oopgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.viklover.oopgame.forms.LoginForm;
import ru.viklover.oopgame.forms.RegisterForm;

@Controller
@RequestMapping("/")
@CrossOrigin
public class MainController {

    @GetMapping
    public String homePage() {
        return "home/index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login/index";
    }

    @PostMapping("/login")
    public String login(LoginForm form) {

        // TODO: check existing form.user and match bcrypted form.password \
        //       and then return JWT token

        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register/index";
    }

    @PostMapping("/register")
    public String register(RegisterForm form) {

        // TODO: validate form fields and redirect to login page if success else return error http code

        return "redirect:/";
    }
}