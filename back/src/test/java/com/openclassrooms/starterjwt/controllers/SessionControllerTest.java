package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.openclassrooms.starterjwt.UtilsForTest;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SessionMapper sessionMapper;
    private static SessionService sessionService;
    private static UserRepository userRepository;
    private static String jwtToken;
    private static Session sessionOne;
    private static Session sessionTwo;
    private static List<Session> sessions;
    private static List<User> users;
    private static Teacher teacher;

    @BeforeAll
    static void setUpAll(@Autowired SessionService sessionService,
                         @Autowired UserRepository userRepository,
                         @Autowired TeacherRepository teacherRepository) {

        SessionControllerTest.sessionService = sessionService;
        SessionControllerTest.userRepository = userRepository;

        User userOne = new User()
                .setEmail("johndoe@gmail.com")
                .setLastName("Doe")
                .setFirstName("John")
                .setPassword("John12345")
                .setAdmin(true);
        userRepository.save(userOne);
        User userTwo = new User()
                .setEmail("janedie@gmail.com")
                .setLastName("Die")
                .setFirstName("Jane")
                .setPassword("Jane12345")
                .setAdmin(false);
        userRepository.save(userTwo);

        Optional<User> optUser = userRepository.findByEmail("johndoe@gmail.com");
        Optional<User> optUserTwo = userRepository.findByEmail("janedie@gmail.com");

        if (optUser.isPresent() && optUserTwo.isPresent()) {
            users = List.of(optUser.get(), optUserTwo.get());
        }

        List<Teacher> teachers =  teacherRepository.findAll();
        teacher = teachers.get(0);

        sessionOne = new Session()
                .setName("First Session")
                .setDate(new Date())
                .setDescription("Welcome beginners")
                .setTeacher(teacher)
                .setUsers(users)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now().plusDays(2));
        sessionOne = sessionService.create(sessionOne);
        sessionTwo = new Session()
                .setName("Second Session")
                .setDate(new Date())
                .setDescription("Welcome intermediate")
                .setTeacher(teacher);
        sessionTwo = sessionService.create(sessionTwo);

        sessions = List.of(sessionOne, sessionTwo);

        jwtToken = UtilsForTest.generateJwtToken("johndoe@gmail.com");
    }

    @AfterAll
    static void cleanUp() {
        sessions.forEach(session -> sessionService.delete(session.getId()));
        users.forEach(user -> userRepository.delete(user));
    }

    @Test
    void findById_shouldUseService_forReturningNotFound_withBadId() throws Exception {
        long idUnknown = sessionTwo.getId()+1;
        this.mockMvc.perform(get("/api/session/{id}", idUnknown).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldUse_forReturningBadRequest_withBadFormatId() throws Exception {
        this.mockMvc.perform(get("/api/session/{id}", "toto").header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void findById_shouldUseService_forReturningResponseOK_withBodySession() throws Exception {
        this.mockMvc.perform(get("/api/session/{id}", String.valueOf(sessionOne.getId())).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teacher_id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
        ;
    }

    @Test
    void findAll_shouldUseService_forReturningResponseOK_withBodySession() throws Exception {
        int expectedIdSessionOne = sessions.get(0).getId().intValue();
        int expectedIdSessionTwo = sessions.get(1).getId().intValue();

        this.mockMvc.perform(get("/api/session/").header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").value(containsInAnyOrder(expectedIdSessionOne, expectedIdSessionTwo)));
    }

    @Test
    void update_shouldUseService_forReturningBadRequest_withBadId() throws Exception {
        SessionDto sessionDto = sessionMapper.toDto(sessions.get(0));
        sessionDto.setName("First Session for beginners");

        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        String body = mapper.writeValueAsString(sessionDto);

        this.mockMvc.perform(put("/api/session/{id}","Toto").header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldUseService_forReturningResponseOK_withSessionUpdated() throws Exception {
        SessionDto sessionDto = sessionMapper.toDto(sessions.get(0));
        sessionDto.setName("Third Session for beginners");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Welcome");

        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        String body = mapper.writeValueAsString(sessionDto);

        this.mockMvc.perform(put("/api/session/{id}",sessions.get(0).getId()).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Third Session for beginners"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Welcome"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(not(sessions.get(0).getUpdatedAt())));
    }

    @Test
    void create_shouldUseService_forReturningResponseOK_withSessionCreated() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Fourth Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Welcome intermediate");
        sessionDto.setTeacher_id(teacher.getId());

        ObjectMapper mapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        String body = mapper.writeValueAsString(sessionDto);

        this.mockMvc.perform(post("/api/session/").header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Fourth Session"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Welcome intermediate"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.teacher_id").value(teacher.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").exists());
    }

    @Test
    void delete_shouldUseService_forReturningBadRequest_withBadFormatId() throws Exception {
        this.mockMvc.perform(delete("/api/session/{id}", "Bad").header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void delete_shouldUseService_forReturningNotFound_withBadId() throws Exception {
        this.mockMvc.perform(delete("/api/session/{id}", String.valueOf(sessionOne.getId() + 3)).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldUseService_forReturningResponseOK() throws Exception {
        this.mockMvc.perform(delete("/api/session/{id}", String.valueOf(sessionOne.getId())).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

}
