package ru.viklover.oopgame.user;

import lombok.AllArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.viklover.oopgame.api.auth.form.LoginForm;
import ru.viklover.oopgame.api.auth.form.RegisterForm;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    public UserRepository userRepository;

    @Transactional
    public Optional<User> create(RegisterForm registerForm) {

        if (!registerForm.getPassword().equals(registerForm.getRepeatedPassword()) ||
                userRepository.checkExistingByUsername(registerForm.getUsername())) {
            return Optional.empty();
        }

        return Optional.of(userRepository.create(registerForm.getUsername(), registerForm.getPassword()));
    }

    @Transactional
    public Optional<User> validateLoginForm(LoginForm loginForm) {

        if (!userRepository.checkExistingByUsername(loginForm.getUsername()))
            return Optional.empty();

        var user = userRepository.findByUsername(loginForm.getUsername());

        if (BCrypt.checkpw(loginForm.getPassword(), user.getPasswordHash()))
            return Optional.of(user);

        return Optional.empty();
    }

    @Transactional
    public Optional<User> findById(Long id) {

        if (!userRepository.checkExistingById(id))
            throw new UserNotFoundException(id);

        return Optional.of(userRepository.findById(id));
    }
}