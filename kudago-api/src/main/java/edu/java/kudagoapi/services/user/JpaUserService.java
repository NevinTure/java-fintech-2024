package edu.java.kudagoapi.services.user;

import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import edu.java.kudagoapi.dtos.user.*;
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
import java.util.Objects;

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
    public ResponseEntity<Object> changePassword(ChangePasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof TokenUser tokenUser) {
            User user = repo.findByName(tokenUser.getUsername()).get();
            validateChangePassword(request, user);
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            repo.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new BadRequestApiException("Authentication must be of type TokenUser");
    }

    private void validateChangePassword(ChangePasswordRequest request, User user) {
        if (user.getSecretKey() == null) {
            throw new BadRequestApiException("2FA must be enabled");
        }
        if (!Objects.equals(request.getCode(), "0000")) {
            throw new BadRequestApiException("2FA code doesn't match");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestApiException("Wrong password");
        }
        if (!Objects.equals(request.getNewPassword(), request.getConfirmPassword())) {
            throw new BadRequestApiException("new_pass and confirm_pass doesn't match");
        }
    }

    @Override
    public ResponseEntity<TwoFAResponse> enable2FA() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof TokenUser tokenUser) {
            User user = repo.findByName(tokenUser.getUsername()).get();
            TwoFAResponse twoFAResponse = createKey(user);
            return ResponseEntity.ok(twoFAResponse);
        }
        throw new BadRequestApiException("Authentication must be of type TokenUser");
    }

    @Override
    public ResponseEntity<Object> disable2FA() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof TokenUser tokenUser) {
            User user = repo.findByName(tokenUser.getUsername()).get();
            UserSecretKey secretKey = user.getSecretKey();
            System.out.println(secretKey);
            if (secretKey != null) {
                userSecretKeyRepo.deleteById(secretKey.getId());
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
            saveKey(user, secret);
            return new TwoFAResponse(secret);
        }
        throw new BadRequestApiException("2FA already enabled");
    }

    private void saveKey(User user, String secret) {
        UserSecretKey userSecretKey = new UserSecretKey(secret);
        userSecretKey.setUser(user);
        userSecretKeyRepo.save(userSecretKey);
    }
}
