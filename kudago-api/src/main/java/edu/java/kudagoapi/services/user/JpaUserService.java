package edu.java.kudagoapi.services.user;

import edu.java.kudagoapi.dtos.LoginRequest;
import edu.java.kudagoapi.dtos.RegisterRequest;
import edu.java.kudagoapi.model.Role;
import edu.java.kudagoapi.model.User;
import edu.java.kudagoapi.repositories.JpaRoleRepository;
import edu.java.kudagoapi.repositories.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JpaUserService implements UserService {

    private final JpaUserRepository repo;
    private final JpaRoleRepository roleRepo;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<Object> register(RegisterRequest request) {
        System.out.println(request);
        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        applyRoles(user);
        repo.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> login(LoginRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()
                        )
                );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void applyRoles(User user) {
        Role userRole = roleRepo.findByName("USER").get();
        user.getRoles().add(userRole);
    }
}
