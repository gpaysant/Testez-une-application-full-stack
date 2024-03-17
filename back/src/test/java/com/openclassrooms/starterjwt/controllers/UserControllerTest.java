package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.UtilsForTest;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static UserRepository userRepository;
    private static String jwtToken;

    private static List<User> users;

    @BeforeAll
    static void setUpAll(@Autowired UserRepository userRepository,
                         @Autowired PasswordEncoder passwordEncoder) {

        UserControllerTest.userRepository = userRepository;

        User userOne = new User()
                .setEmail("johndoe@gmail.com")
                .setLastName("Doe")
                .setFirstName("John")
                .setPassword(passwordEncoder.encode("John12345"))
                .setAdmin(true);
        userRepository.save(userOne);

        User userTwo = new User()
                .setEmail("janedie@gmail.com")
                .setLastName("Die")
                .setFirstName("Jane")
                .setPassword(passwordEncoder.encode("Jane12345"))
                .setAdmin(false);
        userRepository.save(userTwo);

        Optional<User> optUser = userRepository.findByEmail("johndoe@gmail.com");
        Optional<User> optUserTwo = userRepository.findByEmail("janedie@gmail.com");

        if (optUser.isPresent() && optUserTwo.isPresent()) {
            users = new ArrayList<>();
            users.add(optUser.get());
            users.add(optUserTwo.get());
        }

        jwtToken = UtilsForTest.generateJwtToken("janedie@gmail.com");
    }

    @AfterAll
    static void cleanUp() {
        users.forEach(userRepository::delete);
    }

    @Test
    void findById_shouldUseService_forReturningBadRequest_withBadFormatId() throws Exception {

        this.mockMvc.perform(get("/api/user/{id}", "toto").header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest());
    }

    @Test
    void findById_shouldUseService_forReturningNotFound_withUnknowId() throws Exception {
        this.mockMvc.perform(get("/api/user/{id}", users.get(0).getId() + 10).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldUseService_forReturningResponseOK_withBodyUser() throws Exception {
        User user = users.get(0);
        this.mockMvc.perform(get("/api/user/{id}", user.getId()).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.admin").value(user.isAdmin()));
    }

    @Test
    void delete_shouldUseService_forReturningBadRequest_withBadIdFormat() throws Exception {
        this.mockMvc.perform(delete("/api/user/{id}", "toto").header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void delete_shouldUseService_forReturningNotFound_withUnknownId() throws Exception {
        long idUnknown = users.get(0).getId() + 10;
        this.mockMvc.perform(delete("/api/user/{id}", idUnknown).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldUseService_forReturningNotUnauthorized_withBadUser() throws Exception {
        this.mockMvc.perform(delete("/api/user/{id}", users.get(0).getId()).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void delete_shouldUseService_forReturningResponseOK() throws Exception {
        User user = users.get(1);
        this.mockMvc.perform(delete("/api/user/{id}", users.get(1).getId()).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        // to avoid breaking test
        users.remove(1);
        userRepository.save(user);
        Optional<User> userNew = userRepository.findByEmail(user.getEmail());
        userNew.ifPresent(users::add);
    }

}
