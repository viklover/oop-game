package ru.viklover.oopgame.api.auth;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.viklover.oopgame.api.forms.LoginForm;
import ru.viklover.oopgame.response.JwtResponse;
import ru.viklover.oopgame.security.jwt.JwtUtils;
import ru.viklover.oopgame.user.UserRepository;
import ru.viklover.oopgame.user.role.UserRole;


@Controller
@RequestMapping("/login")
@CrossOrigin
@AllArgsConstructor
public class LoginController {

    public UserRepository userRepository;

    public JwtUtils jwtUtils;

    @GetMapping
    public String loginPage() {
        return "login/index";
    }

    @PostMapping
    public ResponseEntity<?> login(LoginForm loginForm) {

        if (!userRepository.checkUserExistingByUsername(loginForm.getUsername())) {
            return ResponseEntity.notFound().build();
        }

        if (!userRepository.validatePassword(loginForm)) {
            return ResponseEntity.badRequest().build();
        }

        var user = userRepository.findByUsername(loginForm.getUsername()).get();
        var jwt = jwtUtils.generateJwtToken(user);

        System.out.println("hello");
        System.out.println(jwt);

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                "Bearer ",
                user.getId(),
                user.getUsername(),
                user.getRoles().stream().map(UserRole::getName).toList()
        ));
    }
}