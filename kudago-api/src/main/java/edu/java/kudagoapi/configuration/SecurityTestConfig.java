package edu.java.kudagoapi.configuration;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Profile("without-security")
public class SecurityTestConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/**"));
    }
}
