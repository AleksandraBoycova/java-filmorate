package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractModel;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        super(userStorage);
        this.userStorage = userStorage;
    }

    @Override
    protected void validate(AbstractModel abstractModel) throws ValidationException {
        User user = (User) abstractModel;
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
        User user   = (User) userStorage.getById(id);
        User friend = (User) userStorage.getById(friendId);
        user.getFriends().add(friend.getId());
        userStorage.update(user);
        friend.getFriends().add(user.getId());
        userStorage.update(friend);
    }

    public void deleteFriend(Long id, Long friendId) throws Exception {
        User user   = (User) userStorage.getById(id);
        User friend = (User) userStorage.getById(friendId);
        if (user.getFriends().contains(friendId) && friend.getFriends().contains(id)) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(id);
            userStorage.update(user);
            userStorage.update(friend);
        } else {
            throw new ValidationException("Эти пользователи не состояли в друзьях");
        }
    }

    public Collection<AbstractModel> getFriendsForUser(Long id) throws Exception {
        User user = (User) userStorage.getById(id);
        Set<Long> friends = user.getFriends();
         return userStorage.getAll().stream().filter(u->friends.contains(u.getId())).collect(Collectors.toList());

    }

    public Collection<AbstractModel> getCommonFriends(Long id, Long otherId) throws Exception {
        User user      = (User) userStorage.getById(id);
        User otherUser = (User) userStorage.getById(otherId);
        Collection<Long> commonFriends = user.getFriends().stream()
                .distinct()
                .filter(otherUser.getFriends()::contains)
                .collect(Collectors.toSet());
        return userStorage.getAll().stream().filter(u->commonFriends.contains(u.getId())).collect(Collectors.toList());

    }
}
