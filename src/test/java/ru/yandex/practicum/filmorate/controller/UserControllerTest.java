package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;

    public static Stream<Arguments> prepareDataForCreateUser() {
        return Stream.of(
                Arguments.of(buildUser("", "login","name", "10.12.1990"), "электронная почта не может быть пустой и должна содержать символ @"),
                Arguments.of(buildUser("email@mail.ru", "login", "name", "10.12.2024"),"дата рождения не может быть в будущем"),
                Arguments.of(buildUser("email@mail.ru", "", "name", "10.12.1990"), "логин не может быть пустым и содержать пробелы")
        );
    }


    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void createUser() throws ValidationException {
        User user1 = userController.createUser(buildUser("email@mail.ru", "login", "name", "13.10.1990"));
        User user2 = userController.createUser(buildUser("email@mail.ru", "login", "", "13.10.1990"));
        assertNotNull(user1.getId());
        assertNotNull(user2.getId());
        assertEquals("login", user2.getName());
    }

    @ParameterizedTest
    @MethodSource("prepareDataForCreateUser")
    void createUserWithError (User user, String expectedErrorMessage) {
        assertThrows(ValidationException.class, ()->userController.createUser(user), expectedErrorMessage);
    }



    @Test
    void updateUser() throws ValidationException {
        User user = buildUser("email@mail.ru", "login", "name", "13.10.1990");
        User createUser = userController.createUser(user);
        createUser.setLogin("new-login");
        User updateUser = userController.updateUser(createUser);
        assertEquals("new-login", updateUser.getLogin());
    }

    @Test
    void getAllUsers() throws ValidationException {
        User user = buildUser("email@mail.ru", "login", "name", "13.10.1990");
        User createUser = userController.createUser(user);
        User user1 = buildUser("email1@mail.ru", "login1", "name1", "13.10.1991");
        User createUser1 = userController.createUser(user);
        Collection<User> allUsers = userController.getAllUsers();
        assertEquals(2, allUsers.size());

    }

    private static User buildUser (String email, String login, String name, String dateString) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(LocalDate
                .parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return user;
    }
}