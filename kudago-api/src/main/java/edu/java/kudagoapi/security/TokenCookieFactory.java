package edu.java.kudagoapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.kudagoapi.configuration.ApplicationConfig;
import edu.java.kudagoapi.dtos.user.LoginRequest;
import edu.java.kudagoapi.utils.Token;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenCookieFactory {

    private final ApplicationConfig config;
    private final ObjectMapper objectMapper;

    public Token create(Authentication authentication, HttpServletRequest request) {
        boolean rememberMe = getRememberMeFromRequest(request);
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime ttl = now.plus(rememberMe
                ? config.tokenAuthentication().rememberMeTtl()
                : config.tokenAuthentication().ttl());
        return new Token(UUID.randomUUID(), authentication.getName(),
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                now, ttl);
    }

    @SneakyThrows
    private boolean getRememberMeFromRequest(HttpServletRequest request) {
        if (request.getContentType().contains("json")) {
            LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
            return loginRequest.isRememberMe();
        }
        return false;
    }
}
