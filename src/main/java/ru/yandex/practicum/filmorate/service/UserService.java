package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) throws ValidationException {
        validate(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) throws Exception {
        validate(user);
        return userStorage.updateUser(user);
    }

    public User deleteUser(Long id) throws Exception {
        return userStorage.deleteUser(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(Long id) throws Exception {
        return userStorage.getUser(id);
    }

    public void validate (User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new  ValidationException ("Email введен не верно");
        }
        if(user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Введен не верный логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Не верная дата рождения");
        }

    }

    public void addFriend(Long id, Long friendId) throws Exception {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        user.getFriends().add(friend.getId());
        userStorage.updateUser(user);
        friend.getFriends().add(user.getId());
        userStorage.updateUser(friend);
    }

    public void deleteFriend(Long id, Long friendId) throws Exception {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        if(user.getFriends().contains(friendId) && friend.getFriends().contains(id)) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(id);
            userStorage.updateUser(user);
            userStorage.updateUser(friend);
        }
        else {
            throw new ValidationException("Эти пользователи не состояли в друзьях");
        }
    }

    public Collection<Long> getFriendsForUser(Long id) throws Exception {
        User user = userStorage.getUser(id);
        return user.getFriends();
    }

    public Collection<Long> getCommonFriends(Long id, Long otherId) throws Exception {
        User user = userStorage.getUser(id);
        User otherUser = userStorage.getUser(otherId);
        return user.getFriends().stream()
                .distinct()
                .filter(otherUser.getFriends()::contains)
                .collect(Collectors.toSet());
    }
}
