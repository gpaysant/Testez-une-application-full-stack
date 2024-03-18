package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private static UserRepository userRepository;
    private static List<User> users;

    @BeforeAll
    static void setUpAll(@Autowired UserRepository userRepository,
                         @Autowired PasswordEncoder passwordEncoder) {
        AuthControllerTest.userRepository = userRepository;

        User userOne = new User()
                .setEmail("johndoe@gmail.com")
                .setLastName("Doe")
                .setFirstName("John")
                .setPassword(passwordEncoder.encode("John12345"))
                .setAdmin(true)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
        userRepository.save(userOne);

        Optional<User> optUser = userRepository.findByEmail("johndoe@gmail.com");

        if (optUser.isPresent()) {
            users = new ArrayList<>();
            users.add(optUser.get());
        }
    }

    @AfterAll
    static void cleanUp() {
        users.forEach(userRepository::delete);
    }

    @Test
    void register_shouldUseRepository_forReturningBadRequest_withExistingEmail() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("johndoe@gmail.com");
        signupRequest.setFirstName("john");
        signupRequest.setLastName("doe");
        signupRequest.setPassword("password123");
        String body = (new ObjectMapper()).writeValueAsString(signupRequest);

        this.mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isBadRequest()).andExpect(content().string(containsString("Error: Email is already taken!")));
    }

    @Test
    void register_shouldUseRepository_forReturningMessageOK_withResponse() throws Exception {
        String emailSignUp = "peterpan@gmail.com";
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(emailSignUp);
        signupRequest.setFirstName("peter");
        signupRequest.setLastName("pan");
        signupRequest.setPassword("password100");
        String body = (new ObjectMapper()).writeValueAsString(signupRequest);

        this.mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isOk()).andExpect(content().string(containsString("User registered successfully!")));

        userRepository.findByEmail(emailSignUp).ifPresent(users::add);
    }

    @Test
    void login_shouldUseRepositoryAuthenticationJwtUtils_forReturningMessageOK_withJwtResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(users.get(0).getEmail());
        loginRequest.setPassword("John12345");
        String body = (new ObjectMapper()).writeValueAsString(loginRequest);

        this.mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(body)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value(users.get(0).getEmail()))
                .andExpect(jsonPath("$.firstName").value(users.get(0).getFirstName()))
                .andExpect(jsonPath("$.lastName").value(users.get(0).getLastName()))
                .andExpect(jsonPath("$.admin").value(users.get(0).isAdmin()));
    }

}
