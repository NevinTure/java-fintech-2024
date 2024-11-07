package edu.java.kudagoapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.kudagoapi.serializers.TokenCookieJweStringSerializer;
import edu.java.kudagoapi.utils.Token;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class TokenCookieSessionAuthenticationStrategy
        implements SessionAuthenticationStrategy {

    private final TokenCookieFactory tokenCookieFactory;

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {
        System.out.println("kek inside");
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            Token token = tokenCookieFactory.create(authentication, request);
            String tokenString = token.toString();
            Cookie cookie = new Cookie("__Host-auth-token", tokenString);
            cookie.setPath("/");
            cookie.setDomain(null);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setMaxAge((int) ChronoUnit.SECONDS.between(OffsetDateTime.now(), token.expiresAt()));
            response.addCookie(cookie);
        }
    }


}
