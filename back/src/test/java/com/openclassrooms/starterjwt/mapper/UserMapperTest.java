package com.openclassrooms.starterjwt.mapper;


import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserMapperTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    public void setup() {
        userMapper = new UserMapperImpl();
    }

    @Test
    public void shouldMapEntityToDto() {
        // given
        long id = 1L;
        String email = "johndoe@gmail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "John12345";
        boolean isAdmin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(2);

        User entity = User.builder()
                .id(id)
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .password(password)
                .admin(isAdmin)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // when
        UserDto aDto = userMapper.toDto(entity);

        // then
        assertThat(aDto)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("email", email)
                .hasFieldOrPropertyWithValue("lastName", lastName)
                .hasFieldOrPropertyWithValue("firstName", firstName)
                .hasFieldOrPropertyWithValue("password", password)
                .hasFieldOrPropertyWithValue("admin", isAdmin)
                .hasFieldOrPropertyWithValue("createdAt", createdAt)
                .hasFieldOrPropertyWithValue("updatedAt", updatedAt);

    }

    @Test
    public void shouldMapEntityToDtoNull() {
        // given
        User entity = null;
        // when
        UserDto aDto = userMapper.toDto(entity);
        // then
        assertThat(aDto).isNull();
    }

    @Test
    public void shouldMapEntityToDtoList() {
        // given
        long id = 1L;
        String email = "johndoe@gmail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "John12345";
        boolean isAdmin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(2);

        User entity = User.builder()
                .id(id)
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .password(password)
                .admin(isAdmin)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        long id2 = 2L;
        String email2 = "janeDae@gmail.com";
        String firstName2 = "Jane";
        String lastName2 = "Dae";
        String password2 = "Jane12345";
        boolean isAdmin2 = false;
        LocalDateTime createdAt2 = LocalDateTime.now();
        LocalDateTime updatedAt2 = LocalDateTime.now().plusDays(3);
        User entitySecond = User.builder()
                .id(id2)
                .email(email2)
                .lastName(lastName2)
                .firstName(firstName2)
                .password(password2)
                .admin(isAdmin2)
                .createdAt(createdAt2)
                .updatedAt(updatedAt2)
                .build();
        List<User> users = Arrays.asList(entity, entitySecond);

        // when
        List<UserDto> aDto = userMapper.toDto(users);

        // then
        assertThat(aDto.get(0))
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("email", email)
                .hasFieldOrPropertyWithValue("lastName", lastName)
                .hasFieldOrPropertyWithValue("firstName", firstName)
                .hasFieldOrPropertyWithValue("password", password)
                .hasFieldOrPropertyWithValue("admin", isAdmin)
                .hasFieldOrPropertyWithValue("createdAt", createdAt)
                .hasFieldOrPropertyWithValue("updatedAt", updatedAt);
        assertThat(aDto.get(1))
                .hasFieldOrPropertyWithValue("id", id2)
                .hasFieldOrPropertyWithValue("email", email2)
                .hasFieldOrPropertyWithValue("lastName", lastName2)
                .hasFieldOrPropertyWithValue("firstName", firstName2)
                .hasFieldOrPropertyWithValue("password", password2)
                .hasFieldOrPropertyWithValue("admin", isAdmin2)
                .hasFieldOrPropertyWithValue("createdAt", createdAt2)
                .hasFieldOrPropertyWithValue("updatedAt", updatedAt2);
    }

    @Test
    public void shouldMapEntityToDtoListNull() {
        // given
        List<User> users = null;

        // when
        List<UserDto> aDto = userMapper.toDto(users);

        // then
        assertThat(aDto).isNull();
    }

    @Test
    public void shouldMapDtoToEntity() {
        // given
        long id = 1L;
        String email = "johndoe@gmail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "John12345";
        boolean isAdmin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(2);

        UserDto userDto = new UserDto(
            id,
            email,
            lastName,
            firstName,
            isAdmin,
            password,
            createdAt,
            updatedAt
        );

        // when
        User user = userMapper.toEntity(userDto);

        // then
        assertThat(user)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("email", email)
                .hasFieldOrPropertyWithValue("lastName", lastName)
                .hasFieldOrPropertyWithValue("firstName", firstName)
                .hasFieldOrPropertyWithValue("password", password)
                .hasFieldOrPropertyWithValue("admin", isAdmin)
                .hasFieldOrPropertyWithValue("createdAt", createdAt)
                .hasFieldOrPropertyWithValue("updatedAt", updatedAt);

    }

    @Test
    public void shouldMapDtoToEntityNull() {
        // given
        UserDto userDto = null;
        // when
        User user = userMapper.toEntity(userDto);

        // then
        assertThat(user).isNull();
    }

    @Test
    public void shouldMapDtoToEntityList() {
        // given
        long id = 1L;
        String email = "johndoe@gmail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "John12345";
        boolean isAdmin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(2);

        UserDto userDto = new UserDto(
                id,
                email,
                lastName,
                firstName,
                isAdmin,
                password,
                createdAt,
                updatedAt
        );

        long id2 = 2L;
        String email2 = "janeDae@gmail.com";
        String firstName2 = "Jane";
        String lastName2 = "Dae";
        String password2 = "Jane12345";
        boolean isAdmin2 = false;
        LocalDateTime createdAt2 = LocalDateTime.now();
        LocalDateTime updatedAt2 = LocalDateTime.now().plusDays(3);
        UserDto userDtoSecond = new UserDto(
                id2,
                email2,
                lastName2,
                firstName2,
                isAdmin2,
                password2,
                createdAt2,
                updatedAt2
        );
        List<UserDto> users = Arrays.asList(userDto, userDtoSecond);

        // when
        List<User> user = userMapper.toEntity(users);

        // then
        assertThat(user.get(0))
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("email", email)
                .hasFieldOrPropertyWithValue("lastName", lastName)
                .hasFieldOrPropertyWithValue("firstName", firstName)
                .hasFieldOrPropertyWithValue("password", password)
                .hasFieldOrPropertyWithValue("admin", isAdmin)
                .hasFieldOrPropertyWithValue("createdAt", createdAt)
                .hasFieldOrPropertyWithValue("updatedAt", updatedAt);

        assertThat(user.get(1))
                .hasFieldOrPropertyWithValue("id", id2)
                .hasFieldOrPropertyWithValue("email", email2)
                .hasFieldOrPropertyWithValue("lastName", lastName2)
                .hasFieldOrPropertyWithValue("firstName", firstName2)
                .hasFieldOrPropertyWithValue("password", password2)
                .hasFieldOrPropertyWithValue("admin", isAdmin2)
                .hasFieldOrPropertyWithValue("createdAt", createdAt2)
                .hasFieldOrPropertyWithValue("updatedAt", updatedAt2);

    }
}
