package edu.java.kudagoapi.controllers;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.model.Role;
import edu.java.kudagoapi.model.User;
import edu.java.kudagoapi.repositories.JpaRoleRepository;
import edu.java.kudagoapi.repositories.JpaUserRepository;
import edu.java.kudagoapi.utils.Token;
import edu.java.kudagoapi.utils.TokenUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.OffsetDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerSystemTest extends IntegrationEnvironment {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JpaUserRepository repo;
    @Autowired
    private JpaRoleRepository roleRepo;
    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    public void init() {
        repo.deleteAll();
        roleRepo.deleteAll();
    }

    @Test
    public void testRegister() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_USER", "test");

        //then
        mvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "test1",
                                  "email": "test1@test.com",
                                  "password": "12345678"
                                }
                                """))
                .andExpect(status().isOk());
        assertThat(repo.findByName("test1")).isPresent();
        assertThat(repo.findByName("test1").get().getEmail())
                .isEqualTo("test1@test.com");
    }

    @Test
    public void testRegisterThanAlreadyExists() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_USER", "test");
        User user = createAndSaveUser("test1", "12345678", List.of(role));

        //then
        mvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                 "name": "test1",
                                 "email": "test1@test.com",
                                 "password": "12345678"
                                }
                                """))
                .andExpect(status().isBadRequest());
        //add message check
    }

    @Test
    public void testRegisterThanInvalidParams() throws Exception {
        //then
        mvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "test1",
                                  "email": "test1@test.com",
                                  "password": "1234"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLogin() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_USER", "test");
        User user = createAndSaveUser("test1", "12345678", List.of(role));

        //then
        mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "test1",
                                  "password": "12345678"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testLoginThenInvalidRequestParams() throws Exception {
        //then
        mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "test1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(
                        """
                                {
                                  "code": 400,
                                  "message": "Invalid request params: [password must not be empty, password must not be null]"
                                }
                                """
                ));
    }

    @Test
    public void testLoginThenInvalidCredentials() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_USER", "test");
        User user = createAndSaveUser("test1", "12345678", List.of(role));

        //then
        mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "test1",
                                  "password": "12345677"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testEnable2FA() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_USER", "test");
        User user = createAndSaveUser("test1", "12345678", List.of(role));
        Authentication auth = createTokenUserAuthentication(user);
        //then
        mvc.perform(patch("/user/enable-2fa")
                        .with(authentication(auth)))
                .andExpect(status().isOk());
    }

    @Test
    public void testEnable2FAThenAlreadyEnabled() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_USER", "test");
        User user = createAndSaveUser("test1", "12345678", List.of(role));
        Authentication auth = createTokenUserAuthentication(user);

        //then
        mvc.perform(patch("/user/enable-2fa")
                        .with(authentication(auth)))
                .andExpect(status().isOk());
        //second time prohibited
        mvc.perform(patch("/user/enable-2fa")
                        .with(authentication(auth)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDisable2FA() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_USER", "test");
        User user = createAndSaveUser("test1", "12345678", List.of(role));
        Authentication auth = createTokenUserAuthentication(user);

        //then
        mvc.perform(patch("/user/disable-2fa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "code": "0000"
                                }
                                """)
                        .with(authentication(auth)))
                .andExpect(status().isOk());
    }

    @Test
    public void testChangePassword() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_USER", "test");
        User user = createAndSaveUser("test1", "12345678", List.of(role));
        Authentication auth = createTokenUserAuthentication(user);

        //then
        mvc.perform(patch("/user/enable-2fa")
                .with(authentication(auth)));
        mvc.perform(patch("/user/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "code": "0000",
                                  "old_pass": "12345678",
                                  "new_pass": "87654321",
                                  "confirm_pass": "87654321"
                                }
                                """)
                        .with(authentication(auth)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test1", password = "12345678")
    public void testChangePasswordThenInvalidParams() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_USER", "test");
        User user = createAndSaveUser("test1", "12345678", List.of(role));

        //then
        mvc.perform(patch("/user/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "code": "1234",
                                  "old_pass": "4",
                                  "new_pass": "87654321",
                                  "confirm_pass": "87654321"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    private User createAndSaveUser(String name, String password, List<Role> roles) {
        User user = new User(
                name,
                UUID.randomUUID() + "@test.com",
                encoder.encode(password)
        );
        user.getRoles().addAll(roles);
        repo.save(user);
        return user;
    }

    private Role createAndSaveRole(String name, String description) {
        Role role = new Role(name, description);
        Optional<Role> foundRole = roleRepo.findByName(name);
        if (foundRole.isEmpty()) {
            roleRepo.save(role);
        } else {
            role = foundRole.get();
        }
        return role;
    }

    public static Authentication createTokenUserAuthentication(User user) {
        Token token = new Token(
                UUID.randomUUID(),
                user.getName(),
                user.getRoles().stream().map(Role::getAuthority).toList(),
                OffsetDateTime.now(),
                OffsetDateTime.MAX);
        TokenUser tokenUser = new TokenUser(
                token.subject(),
                "nopassword",
                true,
                true,
                true,
                true,
                token.authorities().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList(),
                token);
        return new UsernamePasswordAuthenticationToken(tokenUser, user.getPassword(), user.getRoles());
    }
}
