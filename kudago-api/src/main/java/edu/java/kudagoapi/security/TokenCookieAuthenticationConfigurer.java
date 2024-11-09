package edu.java.kudagoapi.security;

import edu.java.kudagoapi.converters.TokenCookieAuthenticationConverter;
import edu.java.kudagoapi.deserializers.TokenCookieJweStringDeserializer;
import edu.java.kudagoapi.model.DeactivatedToken;
import edu.java.kudagoapi.repositories.DeactivatedTokenRepository;
import edu.java.kudagoapi.services.user.TokenAuthenticationUserDetailsService;
import edu.java.kudagoapi.utils.Token;
import edu.java.kudagoapi.utils.TokenUser;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

@RequiredArgsConstructor
public class TokenCookieAuthenticationConfigurer
        extends AbstractHttpConfigurer<TokenCookieAuthenticationConfigurer, HttpSecurity> {

    private final TokenCookieJweStringDeserializer tokenCookieDeserializer;
    private final TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService;
    private final DeactivatedTokenRepository deactivatedTokenRepository;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        builder.logout(logout -> logout.addLogoutHandler(
                        new CookieClearingLogoutHandler("__Host-auth-token")
                )
                .logoutUrl("/user/logout")
                .addLogoutHandler((request, response, authentication) -> {
                    if (authentication != null && authentication.getPrincipal() instanceof TokenUser user) {
                        Token token = user.getToken();
                        DeactivatedToken deactivatedToken = new DeactivatedToken(
                                token.id(),
                                token.expiresAt()
                        );
                        deactivatedTokenRepository.save(deactivatedToken);
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    }
                }));
    }

    @Override
    @SneakyThrows
    public void configure(HttpSecurity builder) {
        AuthenticationFilter cookieAuthenticationFilter = new AuthenticationFilter(
                builder.getSharedObject(AuthenticationManager.class),
                new TokenCookieAuthenticationConverter(tokenCookieDeserializer)
        );
        cookieAuthenticationFilter.setSuccessHandler((request, response, authentication) -> {
        });
        cookieAuthenticationFilter.setFailureHandler(
                new AuthenticationEntryPointFailureHandler(
                        new Http403ForbiddenEntryPoint()
                )
        );
        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(
                tokenAuthenticationUserDetailsService
        );
        builder.addFilterBefore(cookieAuthenticationFilter, LogoutFilter.class)
                .authenticationProvider(authenticationProvider);
    }
}
