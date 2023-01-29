package ru.viklover.oopgame.security.user;

import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.viklover.oopgame.user.UserService;

@Service
@AllArgsConstructor
public class UserDetailsService {

    public UserService userService;

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        var user = userService
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found by id: " + id));

        return UserDetailsImpl.build(user);
    }
}
