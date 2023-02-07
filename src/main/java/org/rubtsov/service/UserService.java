package org.rubtsov.service;

import org.rubtsov.exception.UserNonUniqueException;
import org.rubtsov.model.User;


import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<String> getListAllLogins () {
        return userRepository.getAllUsers().stream()
                .map(User::getLogin)
                .collect(Collectors.toList());
    }
    public void creatNewUser (String login, String password) {
        User user = new User(login, password);

        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login should be defined");
        } else if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password should be defined");
        }
        boolean userExist = this.userRepository
                .getAllUsers()
                .stream()
                .anyMatch(e -> e.equals(user));

        if (userExist) {
            throw new UserNonUniqueException("User already exists");
        }
        this.userRepository.addUser(user);
    }

    public boolean userWithSuchLoginPasswordIsExists(String login, String password) {
        return userRepository.findUserByLoginPassword(login, password).isPresent();
    }

}
