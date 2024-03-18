package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    TeacherRepository teacherRepository;
    TeacherService teacherService;

    List<Teacher> teachers;

    @BeforeEach
    public void init() {
        teacherService = new TeacherService(teacherRepository);

        teachers = Arrays.asList(
                new Teacher(1L,
                        "Doe",
                        "John",
                        LocalDateTime.of(2024, Month.MARCH,8,11,00),
                        LocalDateTime.of(2024, Month.MARCH,9,11,00)
                ),
                new Teacher(2L,
                        "Smith",
                        "Jane",
                        LocalDateTime.of(2024, Month.MARCH,15,12,10),
                        LocalDateTime.of(2024, Month.MARCH,18,13,00)
                )
        );
    }

    @Test
    public void findAll_shouldUseRepository_forReturnAllTeachers() {
        when(teacherRepository.findAll()).thenReturn(teachers);

        final List<Teacher> result = teacherService.findAll();

        verify(teacherRepository).findAll();
        assertThat(result).containsExactlyElementsOf(teachers);
    }

    @Test
    public void findById_shouldUseRepository_forReturnOneTeacher() {
        long id = 2L;
        Teacher teacher = teachers.get(1);
        when(teacherRepository.findById(id)).thenReturn(Optional.ofNullable(teacher));

        final Teacher result = teacherService.findById(id);

        verify(teacherRepository).findById(id);
        assertThat(result).isEqualTo(teacher);
    }

    @Test
    public void findById_shouldUseRepository_forReturnNoTeacher() {
        when(teacherRepository.findById(3L)).thenReturn(Optional.empty());

        final Teacher result = teacherService.findById(3L);

        verify(teacherRepository).findById(3L);
        assertThat(result).isEqualTo(null);
    }

}
