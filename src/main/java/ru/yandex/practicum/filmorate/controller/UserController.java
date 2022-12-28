package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractModel;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("users")
public class UserController extends AbstractController <User>{
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user)
            throws ValidationException {
        return super.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user)
            throws Exception {
        return super.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend (@PathVariable Long id, @PathVariable Long friendId) throws Exception {
        log.info("Добавляем друзей");
        userService.addFriend (id, friendId);
    }

    @DeleteMapping ("/{id}/friends/{friendId}")
    public void deleteFriend (@PathVariable Long id, @PathVariable Long friendId) throws Exception {
        log.info("Удаляем из друзей");
        userService.deleteFriend(id, friendId);
    }

     @GetMapping ("/{id}/friends")
    public Collection<User> getFriendsForUser (@PathVariable Long id) throws Exception {
        log.info("Друзья пользователя {}", id);
       return userService.getFriendsForUser(id);

     }

     @GetMapping ("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends (@PathVariable Long id, @PathVariable Long otherId) throws Exception {
        log.info("Общие друзья пользователей {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
     }
}
