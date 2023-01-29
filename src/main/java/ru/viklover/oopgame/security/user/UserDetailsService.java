package ru.viklover.oopgame.security.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.viklover.oopgame.user.UserRepository;

@Service
public class UserDetailsService {

    public UserRepository userRepository;

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        var user = userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found by id: " + id));

        return UserDetailsImpl.build(user);
    }
}
