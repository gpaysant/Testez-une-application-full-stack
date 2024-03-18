package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    public void init() {
        userService = new UserService(userRepository);

    }

    @Test
    public void delete_shouldUseRepository_forVoid() {
        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    public void findById_shouldUseRepository_forReturningUser() {
        User user = new User()
                .setId(2L)
                .setLastName("Doe")
                .setFirstName("John");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        final User result = userService.findById(user.getId());

        verify(userRepository).findById(user.getId());
        assertThat(result).isEqualTo(user);
    }
}
