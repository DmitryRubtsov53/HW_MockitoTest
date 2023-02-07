package org.rubtsov.service;

import org.rubtsov.model.User;

import java.util.*;

public class UserRepository {
    private final List<User> userList = new ArrayList<>();

    public UserRepository() {
    }

    public Collection<User> getAllUsers () {
        return userList;
    }
    public Optional<User> findUserByLogin (String login) {
        return this.userList.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }
    public Optional<User> findUserByLoginPassword (String login, String password) {
        return this.userList.stream()
                .filter(user -> user.getLogin().equals(login) && user.getPassword().equals(password))
                .findFirst();

    }
    public void addUser (User user) {
        userList.add(user);
    }
}
