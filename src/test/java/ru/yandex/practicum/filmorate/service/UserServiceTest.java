package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.filmorate.AbstractTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest({UserStorage.class, UserService.class})
class UserServiceTest extends AbstractTest {
    @Autowired
    private UserService service;

    @MockBean @Qualifier ("inMemoryUserStorage")
    private UserStorage storage;


    @Test
    void createUser() throws ValidationException {
        User user1 = buildUser("user1@mail.ru", "user1", "Anna", "13.10.1990");
        when(storage.create(any())).thenReturn(user1);
        User user = (User) service.create(user1);
        assertEquals(user1, user);
    }

    @Test
    void updateUser() throws Exception {
        User user1 = buildUser("user1@mail.ru", "user1", "Anna", "13.10.1990");
        when(storage.update(any())).thenReturn(user1);
        User user = service.update(user1);
        assertEquals(user1, user);
    }

    @Test
    void deleteUser() throws Exception {
        User user1 = buildUser("user1@mail.ru", "user1", "Anna", "13.10.1990");
        user1.setId(1L);
        when(storage.delete(any())).thenReturn(user1);
        User user = service.delete(1L);
        assertEquals(user1, user);
    }

    @Test
    void getAllUsers() {
        when(storage.getAll()).thenReturn(createUsers());
        Collection<User> allUsers = service.getAll();
        assertEquals(6, allUsers.size());
    }

    @ParameterizedTest
    @MethodSource("prepareDataForCreateUser")
    void createUserWithError (User user, String expectedErrorMessage) {
        assertThrows(ValidationException.class, ()->service.create(user), expectedErrorMessage);
    }

    @Test
    void addFriend() throws Exception {
        User user1 = buildUser("user1@mail.ru", "user1", "Anna", "13.10.1990");
        User user2 = buildUser("user2@mail.ru", "user2", "Anton", "13.10.1968");
        user1.setId(1L);
        user2.setId(2L);
        user1.getFriends().add(2L);
        user2.getFriends().add(1L);
        when(storage.getById(any())).thenReturn(Optional.of(user1)).thenReturn(Optional.of(user2));
        when(storage.update(any())).thenReturn(user1).thenReturn(user2);
        service.addFriend(1L, 2L);
        verify(storage, times(2)).getById(any());
        verify(storage, times(2)).update(any());

    }

    @Test
    void deleteFriend() throws Exception {
        User user1 = buildUser("user1@mail.ru", "user1", "Anna", "13.10.1990");
        User user2 = buildUser("user2@mail.ru", "user2", "Anton", "13.10.1968");
        user1.setId(1L);
        user2.setId(2L);
        user1.getFriends().add(2L);
        user2.getFriends().add(1L);
        when(storage.getById(any())).thenReturn(Optional.of(user1)).thenReturn(Optional.of(user2));
        when(storage.update(any())).thenReturn(user1).thenReturn(user2);
        service.deleteFriend(1L, 2L);
        verify(storage, times(2)).getById(any());
        verify(storage, times(2)).update(any());
    }

    @Test
    void deleteFriendWithError() throws Exception {
        User user1 = buildUser("user1@mail.ru", "user1", "Anna", "13.10.1990");
        User user2 = buildUser("user2@mail.ru", "user2", "Anton", "13.10.1968");
        user1.setId(1L);
        user2.setId(2L);
        when(storage.getById(any())).thenReturn(Optional.of(user1)).thenReturn(Optional.of(user2));
        when(storage.update(any())).thenReturn(user1).thenReturn(user2);
        assertThrows(ValidationException.class,()->service.deleteFriend(1L, 2L));
        verify(storage, times(2)).getById(any());
        verify(storage, times(0)).update(any());
    }

    @Test
    void getFriendsForUser() throws Exception {
        User user3 = buildUser("user3@mail.ru", "user3", "Anna", "13.10.1990");
        user3.getFriends().addAll(Set.of(1L,2L));
        User user1 = buildUser("user1@mail.ru", "user1", "Anna", "13.10.1990");
        User user2 = buildUser("user2@mail.ru", "user2", "Gosha", "13.10.1994");
        user1.setId(1L);
        user2.setId(2L);
        when(storage.getById(any())).thenReturn(Optional.of(user3));
        when(storage.getAll()).thenReturn(List.of(user1, user2));
        Collection<User> friendsForUser = service.getFriendsForUser(1L);
        assertEquals(2, friendsForUser.size());
        assertTrue(friendsForUser.stream().map(User::getId).collect(Collectors.toList()).containsAll(Set.of(1L,2L)));
    }

    @Test
    void getCommonFriends() throws Exception {
        User user1 = buildUser("user1@mail.ru", "user1", "Anna", "13.10.1990");
        User user3 = buildUser("user3@mail.ru", "user3", "Gosha", "13.10.1994");
        User user2 = buildUser("user2@mail.ru", "user2", "Gosha", "13.10.1994");
        user1.setId(1L);
        user2.setId(2L);
        user3.setId(3L);

        user1.getFriends().add(2L);
        user1.getFriends().add(3L);

        user3.getFriends().add(1L);
        user3.getFriends().add(2L);

        when(storage.getById(any())).thenReturn(Optional.of(user1)).thenReturn(Optional.of(user3));
        when(storage.getAll()).thenReturn(List.of(user1, user2, user3));
        Collection<User> commonFriends = service.getCommonFriends(1L, 3L);
        assertEquals(1, commonFriends.size());
        assertTrue(commonFriends.stream().map(User::getId).collect(Collectors.toList()).contains(2L));
    }

    public static Stream<Arguments> prepareDataForCreateUser() {
        return Stream.of(
                Arguments.of(buildUser("", "login","name", "10.12.1990"), "электронная почта не может быть пустой и должна содержать символ @"),
                Arguments.of(buildUser("email@mail.ru", "login", "name", "10.12.2024"),"дата рождения не может быть в будущем"),
                Arguments.of(buildUser("email@mail.ru", "", "name", "10.12.1990"), "логин не может быть пустым и содержать пробелы")
        );
    }

    private List<User> createUsers () {
        User user1 = buildUser("user1@mail.ru", "user1", "Anna", "13.10.1990");
        User user2 = buildUser("user2@mail.ru", "user2", "Anton", "13.10.1968");
        User user3 = buildUser("user3@mail.ru", "user3", "Gosha", "13.10.1994");
        User user4 = buildUser("user4@mail.ru", "user4", "Masha", "14.10.1978");
        User user5 = buildUser("user5@mail.ru", "user5", "Ruslan", "15.10.2000");
        User user6 = buildUser("user6@mail.ru", "user6", "Alena", "13.11.1996");
        user1.setId(1L);
        user2.setId(2L);
        user3.setId(3L);
        user4.setId(4L);
        user5.setId(5L);
        user6.setId(6L);

        user1.getFriends().add(2L);
        user1.getFriends().add(3L);

        user3.getFriends().add(1L);
        user3.getFriends().add(4L);
        user3.getFriends().add(5L);
        user3.getFriends().add(2L);

        user2.getFriends().add(1L);
        user2.getFriends().add(3L);

        user4.getFriends().add(3L);
        user5.getFriends().add(3L);

        return List.of(user1, user2, user3, user4, user5, user6);
    }


}