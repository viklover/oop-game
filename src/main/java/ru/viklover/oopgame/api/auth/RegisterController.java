package ru.viklover.oopgame.api.auth;

import lombok.AllArgsConstructor;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ru.viklover.oopgame.api.forms.RegisterForm;
import ru.viklover.oopgame.security.annotations.AnonymousUser;
import ru.viklover.oopgame.user.UserService;

@Controller
@RequestMapping("/register")
@CrossOrigin
@AllArgsConstructor
@AnonymousUser
public class RegisterController {

    public UserService userService;

    @GetMapping
    public String registerPage() {
        return "register/index";
    }

    @PostMapping
    public String register(HttpServletResponse response,
                           RegisterForm registerForm) {

        if (userService.create(registerForm).isPresent()) {
            response.setStatus(HttpServletResponse.SC_SEE_OTHER);
            return "redirect:/login";
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        return "register/index";
    }
}