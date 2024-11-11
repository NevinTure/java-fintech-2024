package edu.java.kudagoapi.security;

import edu.java.kudagoapi.IntegrationEnvironment;
import edu.java.kudagoapi.controllers.UserControllerSystemTest;
import edu.java.kudagoapi.model.Role;
import edu.java.kudagoapi.model.User;
import edu.java.kudagoapi.repositories.JpaRoleRepository;
import edu.java.kudagoapi.repositories.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoleTest extends IntegrationEnvironment {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JpaUserRepository userRepo;
    @Autowired
    private JpaRoleRepository roleRepo;
    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    public void init() {
        userRepo.deleteAll();
        roleRepo.deleteAll();
    }

    @Test
    public void testUserRolePermissions() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_USER", "test");
        User user = createAndSaveUser("test1", "12345678", List.of(role));
        Authentication auth = UserControllerSystemTest.createTokenUserAuthentication(user);

        //then
        mvc.perform(get("/api/v1/places/categories")
                        .with(authentication(auth)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUserRoleRestrictions() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_USER", "test");
        User user = createAndSaveUser("test1", "12345678", List.of(role));
        Authentication auth = UserControllerSystemTest.createTokenUserAuthentication(user);

        //then
        mvc.perform(get("/api/v1/locations")
                        .with(authentication(auth)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAdminRolePermissions() throws Exception {
        //given
        Role role = createAndSaveRole("ROLE_ADMIN", "test");
        User user = createAndSaveUser("test1", "12345678", List.of(role));
        Authentication auth = UserControllerSystemTest.createTokenUserAuthentication(user);
        //then
        mvc.perform(get("/api/v1/places/categories")
                        .with(authentication(auth)))
                .andExpect(status().isOk());
        mvc.perform(get("/api/v1/locations")
                        .with(authentication(auth)))
                .andExpect(status().isOk());
    }

    private User createAndSaveUser(String name, String password, List<Role> roles) {
        User user = new User(
                name,
                UUID.randomUUID() + "@test.com",
                encoder.encode(password)
        );
        user.getRoles().addAll(roles);
        userRepo.save(user);
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
}
