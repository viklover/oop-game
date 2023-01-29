package ru.viklover.oopgame.api.auth;

import lombok.AllArgsConstructor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.viklover.oopgame.user.UserService;
import ru.viklover.oopgame.api.forms.LoginForm;
import ru.viklover.oopgame.security.jwt.JwtUtils;
import ru.viklover.oopgame.security.annotations.AnonymousUser;

@Controller
@RequestMapping("/login")
@CrossOrigin
@AllArgsConstructor
@AnonymousUser
public class LoginController {

    public UserService userService;

    public JwtUtils jwtUtils;

    @GetMapping
    public String loginPage() {
        return "login/index";
    }

    @PostMapping
    public String login(LoginForm loginForm, HttpServletResponse response) {

        var userOptional = userService.validateLoginForm(loginForm);

        if (userOptional.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "redirect:/login";
        }

        var user = userOptional.get();

        var cookie = new Cookie("auth_token", jwtUtils.generateJwtToken(user));
        cookie.setMaxAge((int) jwtUtils.getJwtExpirationSeconds());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_SEE_OTHER);

        return "redirect:/";
    }
}