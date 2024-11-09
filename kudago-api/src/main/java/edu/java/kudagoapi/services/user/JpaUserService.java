package edu.java.kudagoapi.services.user;

import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import edu.java.kudagoapi.dtos.RegisterRequest;
import edu.java.kudagoapi.dtos.TwoFAResponse;
import edu.java.kudagoapi.exceptions.BadRequestApiException;
import edu.java.kudagoapi.model.*;
import edu.java.kudagoapi.repositories.*;
import edu.java.kudagoapi.utils.TokenUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JpaUserService implements UserService {

    private final JpaUserRepository repo;
    private final JpaRoleRepository roleRepo;
    private final UserSecretKeyRepository userSecretKeyRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Object> register(RegisterRequest request) {
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
    public ResponseEntity<TwoFAResponse> enable2FA() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getClass());
        if (authentication.getPrincipal() instanceof TokenUser tokenUser) {
            User user = repo.findByName(tokenUser.getToken().subject()).get();
            TwoFAResponse twoFAResponse = createKey(user);
            return ResponseEntity.ok(twoFAResponse);
        }
        throw new BadRequestApiException("Authentication must be of type TokenUser");
    }

    @Override
    public ResponseEntity<Object> disable2FA() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof TokenUser tokenUser) {
            User user = repo.findByName(tokenUser.getToken().subject()).get();
            if (user.getSecretKey() != null) {
                userSecretKeyRepo.deleteById(user.getSecretKey().getId());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new BadRequestApiException("Authentication must be of type TokenUser");
    }

    private void applyRoles(User user) {
        Role userRole = roleRepo.findByName("USER").get();
        user.getRoles().add(userRole);
    }

    private TwoFAResponse createKey(User user) {
        if (user.getSecretKey() == null) {
            String secret = TimeBasedOneTimePasswordUtil.generateBase32Secret();
            UserSecretKey userSecretKey = new UserSecretKey(secret);
            userSecretKey.setUser(user);
            userSecretKeyRepo.save(userSecretKey);
            return new TwoFAResponse(secret);
        }
        throw new BadRequestApiException("2FA already enabled");
    }
}
