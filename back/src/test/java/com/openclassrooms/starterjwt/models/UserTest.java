package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    public void testObjectMethod() {
        User user = new User("johndoe@gmail.com", "doe", "john", "password1234", false);
        User anotherUser = new User("johndoe@gmail.com", "doe", "john", "password1234", false);
        assertThat(user.equals(anotherUser)).isTrue();
        assertThat(user.hashCode() == anotherUser.hashCode()).isTrue();
        String toStringExpected = "User(id=null, email=johndoe@gmail.com, lastName=doe, firstName=john, password=password1234, admin=false, createdAt=null, updatedAt=null)";
        assertThat(user.toString()).isEqualTo(toStringExpected);

        User userBig = new User(1L, "janedae@gmail.com", "dae", "jane", "password1234",
                true, LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        assertThat(userBig.equals(anotherUser)).isFalse();
        assertThat(userBig.equals(anotherUser)).isFalse();
    }
    @Test
    public void testAccessor() {
        long id = 1L;
        String email = "johndoe@gmail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "John12345";
        boolean isAdmin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(2);
        User user = new User()
                .setId(id)
                .setEmail(email)
                .setLastName(lastName)
                .setFirstName(firstName)
                .setPassword(password)
                .setAdmin(isAdmin)
                .setCreatedAt(createdAt)
                .setUpdatedAt(updatedAt);
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getLastName()).isEqualTo(lastName);
        assertThat(user.getFirstName()).isEqualTo(firstName);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.isAdmin()).isEqualTo(isAdmin);
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAt);


    }

    @Test
    public void testBuilder() {
        long id = 1L;
        String email = "johndoe@gmail.com";
        String firstName = "John";
        String lastName = "Doe";
        String password = "John12345";
        boolean isAdmin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now().plusDays(2);

        User person = User.builder()
                .id(id)
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .password(password)
                .admin(isAdmin)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        assertThat(person).isInstanceOf(User.class);

        String toStringExpected = "User(id=1, email=johndoe@gmail.com, lastName=Doe, firstName=John, password=John12345, admin=true, createdAt=" + createdAt + ", updatedAt=" + updatedAt + ")";
        assertThat(person.toString()).isEqualTo(toStringExpected);
    }
}
