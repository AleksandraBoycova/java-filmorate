package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService <User>{
    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("dbUserStorage") UserStorage userStorage) {
        super(userStorage);
        this.userStorage = userStorage;
    }

    @Override
    protected void validate(User abstractModel) throws ValidationException {
        User user = abstractModel;
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException(user, "Email введен не верно", "email", user.getEmail());
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException(user, "Введен не верный логин", "login", user.getLogin());
        }
        if (user.getName()==null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException(user, "Не верная дата рождения", "birthday", user.getBirthday());
        }

    }

    public void addFriend(Long id, Long friendId) throws Exception {
        Optional<User> user = userStorage.getById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        Optional<User> friend = userStorage.getById(friendId);
        if (friend.isEmpty()) {
            throw new NotFoundException("User with id " + friendId + " not found");
        }
        user.get().getFriends().add(friend.get().getId());
        userStorage.update(user.get());
    }

    public void deleteFriend(Long id, Long friendId) throws Exception {
        Optional<User> user = userStorage.getById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        Optional<User> friend = userStorage.getById(friendId);
        if (friend.isEmpty()) {
            throw new NotFoundException("User with id " + friendId + " not found");
        }
        if (user.get().getFriends().contains(friendId) && friend.get().getFriends().contains(id)) {
            user.get().getFriends().remove(friendId);
            userStorage.update(user.get());
        } else {
            throw new ValidationException("Эти пользователи не состояли в друзьях");
        }
    }

    public Collection<User> getFriendsForUser(Long id) throws Exception {
        Optional<User> user = userStorage.getById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        Set<Long> friends = user.get().getFriends();
         return userStorage.getAll().stream().filter(u->friends.contains(u.getId())).collect(Collectors.toList());

    }

    public Collection<User> getCommonFriends(Long id, Long otherId) throws Exception {
        Optional<User> user = userStorage.getById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        Optional<User> otherUser = userStorage.getById(otherId);
        if (otherUser.isEmpty()) {
            throw new NotFoundException("User with id " + otherId + " not found");
        }
        Collection<Long> commonFriends = user.get().getFriends().stream()
                .distinct()
                .filter(otherUser.get().getFriends()::contains)
                .collect(Collectors.toSet());
        return userStorage.getAll().stream().filter(u->commonFriends.contains(u.getId())).collect(Collectors.toList());

    }
}
