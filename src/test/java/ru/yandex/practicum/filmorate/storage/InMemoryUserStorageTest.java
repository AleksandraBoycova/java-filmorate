package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.yandex.practicum.filmorate.AbstractTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebMvcTest(InMemoryUserStorage.class)
class InMemoryUserStorageTest extends AbstractTest {
    @Autowired
    private InMemoryUserStorage storage;

    @BeforeEach
    void setUp() {
        User user1 = storage.createUser(buildUser("user1@mail.ru", "user1", "Anna", "13.10.1990"));
        User user2 = storage.createUser(buildUser("user2@mail.ru", "user2", "Anton", "13.10.1968"));
        User user3 = storage.createUser(buildUser("user3@mail.ru", "user3", "Gosha", "13.10.1994"));
        storage.createUser(user1);
        storage.createUser(user2);
        storage.createUser(user3);

    }

    @AfterEach
    void tearDown() {
        storage.clearStorage();
    }

    @Test
    void createUser() throws ValidationException {
        User user1 = storage.createUser(buildUser("email@mail.ru", "login", "name", "13.10.1990"));
        assertNotNull(user1.getId());
    }

    @Test
    void updateUser() throws Exception {
        User user = buildUser("email@mail.ru", "login", "name", "13.10.1990");
        User createUser = storage.createUser(user);
        createUser.setLogin("new-login");
        User updateUser = storage.updateUser(createUser);
        assertEquals("new-login", updateUser.getLogin());
    }

    @Test
    void getAllUsers() throws ValidationException {
        Collection<User> allUsers = storage.getAllUsers();
        assertEquals(6, allUsers.size());
    }
}