package edu.java.kudagoapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.kudagoapi.deserializers.TokenCookieJweStringDeserializer;
import edu.java.kudagoapi.repositories.DeactivatedTokenRepository;
import edu.java.kudagoapi.services.user.TokenAuthenticationUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public UsernamePasswordAuthenticationFilter jsonAuthenticationFilter(
            ObjectMapper objectMapper, LocalValidatorFactoryBean validator) {
        return new JsonUsernamePasswordAuthenticationFilter(
                authenticationManager(), objectMapper, validator
        );
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("USER")
                .build();
    }

    @Bean
    public TokenCookieAuthenticationConfigurer tokenAuthenticationConfigurer(
            TokenCookieJweStringDeserializer tokenCookieDeserializer,
            TokenAuthenticationUserDetailsService tokenUserDetailsService,
            DeactivatedTokenRepository deactivatedTokenRepository
    ) {
        return new TokenCookieAuthenticationConfigurer(
                tokenCookieDeserializer, tokenUserDetailsService, deactivatedTokenRepository);
    }

    @Bean
    @Profile("!without-security")
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           TokenCookieAuthenticationConfigurer tokenAuthenticationConfigurer,
                                           TokenCookieSessionAuthenticationStrategy tokenAuthenticationStrategy,
                                           UsernamePasswordAuthenticationFilter authenticationFilter
    ) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(login ->
                        login
                                .loginPage("/login")
                                .permitAll()
                                .successHandler((request, response, authentication) -> {
                                    System.out.println("inside kek");
                                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/locations/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1/places/**").hasRole("USER")
                                .anyRequest().authenticated())
                .securityMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/register")))
//                .securityMatcher(new NegatedRequestMatcher(new AntPathRequestMatcher("/login")))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionAuthenticationStrategy(tokenAuthenticationStrategy));
        http.with(tokenAuthenticationConfigurer, Customizer.withDefaults());
        return http.build();
    }
}
