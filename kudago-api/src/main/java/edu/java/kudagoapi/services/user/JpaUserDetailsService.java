package edu.java.kudagoapi.services.user;

import edu.java.kudagoapi.model.User;
import edu.java.kudagoapi.repositories.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final JpaUserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOp = repo.findByName(username);
        if (userOp.isPresent()) {
            return new UserDetailsImpl(userOp.get());
        }
        throw new UsernameNotFoundException(String.format("User with name: %s not found", username));
    }
}
