package edu.java.kudagoapi.services.user;

import edu.java.kudagoapi.model.DeactivatedToken;
import edu.java.kudagoapi.repositories.DeactivatedTokenRepository;
import edu.java.kudagoapi.utils.Token;
import edu.java.kudagoapi.utils.TokenUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final DeactivatedTokenRepository deactivatedTokenRepo;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {
            Optional<DeactivatedToken> deactivatedTokenOp = deactivatedTokenRepo.findById(token.id());
            return new TokenUser(
                    token.subject(),
                    "nopassword",
                    true,
                    true,
                    deactivatedTokenOp.isEmpty() && token.expiresAt().isAfter(OffsetDateTime.now()),
                    true,
                    token.authorities().stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList(),
                    token
            );
        }
        throw new AccessDeniedException("Principal must be of type Token");
    }
}
