package edu.java.kudagoapi.services.user;

import edu.java.kudagoapi.dtos.RegisterRequest;
import edu.java.kudagoapi.model.Role;
import edu.java.kudagoapi.model.User;
import edu.java.kudagoapi.repositories.JpaRoleRepository;
import edu.java.kudagoapi.repositories.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JpaUserService implements UserService, UserDetailsService {

    private final JpaUserRepository repo;
    private final JpaRoleRepository roleRepo;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Object> register(RegisterRequest request) {
        User user = mapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        applyRoles(user);
        repo.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void applyRoles(User user) {
        Role userRole = roleRepo.findByName("USER").get();
        user.getRoles().add(userRole);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOp = repo.findByName(username);
        if (userOp.isPresent()) {
            return new UserDetailsImpl(userOp.get());
        }
        throw new UsernameNotFoundException(String.format("User with name: %s not found", username));
    }
}
