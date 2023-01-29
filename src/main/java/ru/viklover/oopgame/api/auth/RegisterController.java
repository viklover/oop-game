package ru.viklover.oopgame.api.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.viklover.oopgame.api.forms.RegisterForm;
import ru.viklover.oopgame.user.UserRepository;

@Controller
@RequestMapping("/register")
@CrossOrigin
@AllArgsConstructor
public class RegisterController {

    public UserRepository userRepository;

    @GetMapping
    public String registerPage(HttpServletRequest request) {

        if (request.isRequestedSessionIdValid()) {
            return "redirect:/";
        }

        return "register/index";
    }

    @PostMapping
    public String register(HttpServletRequest request,
                           RegisterForm registerForm) {

        if (request.isUserInRole("user")) {
            return "redirect:/";
        }

        if (userRepository.create(registerForm).isEmpty())
            return "register/index";

        return "redirect:/login";
    }
}