package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    @Mock
    SessionRepository sessionRepository;
    @Mock
    UserRepository userRepository;

    Session session;
    User user;
    SessionService sessionService;

    @BeforeEach
    public void init() {
        sessionService = new SessionService(sessionRepository,userRepository);

        user = new User()
                .setId(2L)
                .setLastName("Doe")
                .setFirstName("John");

        User userSession = new User()
                .setId(3L)
                .setLastName("Dae")
                .setFirstName("Jane");
        List<User> users = new ArrayList<>();
        users.add(userSession);

        session = new Session()
                .setId(1L)
                .setName("First Session")
                .setDate(new Date())
                .setDescription("Welcome")
                .setUsers(users);
    }

    @Test
    public void participate_shouldUseRepository_forReturningNotFoundException_withNoUser() {
        long idSession = session.getId();
        long idUser = user.getId();
        when(sessionRepository.findById(idSession)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(idUser)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(idSession,idUser));

        verify(sessionRepository).findById(idSession);
        verify(userRepository).findById(idUser);
    }

    @Test
    public void participate_shouldUseRepository_forReturningNotFoundException_withNoSession() {
        long idSession = session.getId();
        long idUser = user.getId();
        when(sessionRepository.findById(idSession)).thenReturn(Optional.empty());
        when(userRepository.findById(idUser)).thenReturn(Optional.ofNullable(user));

        assertThrows(NotFoundException.class, () -> sessionService.participate(idSession,idUser));

        verify(sessionRepository).findById(idSession);
        verify(userRepository).findById(idUser);
    }

    @Test
    public void participate_shouldUseRepository_forReturningBadRequestException() {
        long idSession = session.getId();
        User otherUser = new User()
                .setId(3L)
                .setLastName("Doe")
                .setFirstName("John");
        long idUser = otherUser.getId();

        when(sessionRepository.findById(idSession)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(idUser)).thenReturn(Optional.ofNullable(otherUser));

        assertThrows(BadRequestException.class, () -> sessionService.participate(idSession,idUser));

        verify(sessionRepository).findById(idSession);
        verify(userRepository).findById(idUser);
    }

    @Test
    public void participate_shouldUseRepository_forSavingSession() {
        long idSession = session.getId();
        long idUser = user.getId();
        when(sessionRepository.findById(idSession)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(idUser)).thenReturn(Optional.ofNullable(user));

        sessionService.participate(idSession,idUser);

        verify(sessionRepository).findById(idSession);
        verify(userRepository).findById(idUser);
        verify(sessionRepository, times(1)).save(session);

        assertThat(session.getUsers().size()).isEqualTo(2);
        assertThat(session.getUsers()).contains(user);
    }

    @Test
    public void noLongerParticipate_shouldUseRepository_forReturningNotFoundException_withNoSession() {
        long idSession = session.getId();
        long idUser = user.getId();
        when(sessionRepository.findById(idSession)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(idSession,idUser));

        verify(sessionRepository).findById(idSession);
    }


    @Test
    public void noLongerParticipate_shouldUseRepository_forReturningBadRequestException() {
        long idSession = session.getId();
        long idUser = user.getId();
        when(sessionRepository.findById(idSession)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(idSession,idUser));

        verify(sessionRepository).findById(idSession);

    }

    @Test
    public void noLongerParticipate_shouldUseRepository_forSavingSession() {
        long idSession = session.getId();
        long idUser = user.getId();
        session.getUsers().add(user);

        when(sessionRepository.findById(idSession)).thenReturn(Optional.ofNullable(session));

        sessionService.noLongerParticipate(idSession,idUser);

        verify(sessionRepository).findById(idSession);
        verify(sessionRepository, times(1)).save(session);

        assertThat(session.getUsers().size()).isEqualTo(1);
        assertThat(session.getUsers()).doesNotContain(user);
    }


}
