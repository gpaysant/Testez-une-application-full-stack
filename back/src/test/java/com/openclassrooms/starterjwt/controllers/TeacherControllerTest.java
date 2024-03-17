package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.UtilsForTest;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherService teacherService;
    private static UserRepository userRepository;
    private static TeacherRepository teacherRepository;
    private static String jwtToken;
    private static List<Teacher> teachers;

    @BeforeAll
    static void setUpAll(@Autowired TeacherRepository teacherRepository,
                         @Autowired UserRepository userRepository) {

        TeacherControllerTest.teacherRepository = teacherRepository;
        TeacherControllerTest.userRepository = userRepository;

        Teacher teacherOne = new Teacher()
                .setLastName("Doe")
                .setFirstName("John")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now().plusDays(2));
        teacherOne = teacherRepository.save(teacherOne);
        Teacher teacherSecond = new Teacher()
                .setLastName("Dae")
                .setFirstName("Jane")
                . setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now().plusDays(2));
        teacherSecond = teacherRepository.save(teacherSecond);
        teachers = List.of(teacherOne, teacherSecond);

        User userOne = new User()
                .setEmail("johndoe@gmail.com")
                .setLastName("Doe")
                .setFirstName("John")
                .setPassword("John12345")
                .setAdmin(true);
        userRepository.save(userOne);

        jwtToken = UtilsForTest.generateJwtToken("johndoe@gmail.com");
    }

    @AfterAll
    static void cleanUp() {
        teacherRepository.deleteAll(teachers);
        Optional<User> userToRemove = userRepository.findByEmail("johndoe@gmail.com");
        userToRemove.ifPresent(userRepository::delete);
    }

    @Test
    void findAll_shouldUseService_forReturningResponseOK_withBodyTeachers() throws Exception {
        int expectedIdTeacherOne = teachers.get(0).getId().intValue();
        int expectedIdTeacherTwo = teachers.get(1).getId().intValue();
        this.mockMvc.perform(get("/api/teacher/").header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").value(hasItems(equalTo(expectedIdTeacherOne), equalTo(expectedIdTeacherTwo))));
    }

    @Test
    void findById_shouldUseService_forReturningBadRequest() throws Exception {
        this.mockMvc.perform(get("/api/teacher/{id}", "titi").header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest());
    }

    @Test
    void findById_shouldUseService_forReturningNotFound_withUnknowId() throws Exception {
        long idUnknown = teachers.get(1).getId() + 1;
        this.mockMvc.perform(get("/api/teacher/{id}", idUnknown).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldUseService_forReturningResponseOK_withBodyTeacher() throws Exception {
        Teacher teacherOne = teachers.get(0);
        this.mockMvc.perform(get("/api/teacher/{id}", teacherOne.getId()).header("Authorization", "Bearer " + jwtToken).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teacherOne.getId()))
                .andExpect(jsonPath("$.lastName").value(teacherOne.getLastName()))
                .andExpect(jsonPath("$.firstName").value(teacherOne.getFirstName()));
    }
}
