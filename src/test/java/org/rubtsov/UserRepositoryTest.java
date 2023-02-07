package org.rubtsov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.rubtsov.model.User;
import org.rubtsov.service.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    private UserRepository userRepository = new UserRepository();
    private final List<User> userList = new ArrayList<>();
    private User user;

    @BeforeEach
    public void setUp() {
        userList.add(new User("Login", "password"));
        user = new User("Login", "password");
    }
   @Test
   @DisplayName("1. Получение пустого списка пользователей → должен возвращаться пустой список.")
   public void getEmptyUsersList () {
        assertEquals(userRepository.getAllUsers(), new ArrayList<User>(List.of()),"List is not empty.");
   }
    @Test
    @DisplayName("2. Получение списка пользователей при изначально заполненном сервисе → " +
            "должны возвращаться те же самые пользователи которых добавляли.")
    public void getNotEmptyUsersList () {
        userRepository.addUser(user);
        assertEquals(userList, userRepository.getAllUsers(),"List is empty.");
    }
    @Test
    @DisplayName("3. Поиск пользователя по логину → в случае если такой пользователь есть.")
    public void findUserByLoginIfSuchUserExists () {
        userRepository.addUser(user);
        Optional<User> userOptional = Optional.of(user);
        assertEquals(userRepository.findUserByLogin("Login"), userOptional, "User is not exists.");
    }
    @Test
    @DisplayName("4. Поиск пользователя по логину → в случае когда такого пользователя нет.")
    public void findUserByLoginIfSuchUserNotExists () {
        userRepository.addUser(user);
        user = new User();
        Optional<User> userOptional = Optional.of(user);
        assertNotEquals(userRepository.findUserByLogin("Login"), userOptional, "User is exists.");
    }
    @Test
    @DisplayName("5. Поиск пользователя по логину и паролю → в случае если такой пользователь есть.")
    public void findUserByLoginPasswordIfSuchUserExists () {
        userRepository.addUser(user);
        Optional<User> userOptional = Optional.of(user);
        assertEquals(userRepository.findUserByLoginPassword("Login","password"), userOptional, "User is not exists.");
    }
    @Test
    @DisplayName("6. Поиск пользователя по логину и паролю → в случае если такого пользователя нет.")
    public void findUserByLoginPasswordIfSuchUserNotExists () {
        userRepository.addUser(user);
        user = new User();
        Optional<User> userOptional = Optional.of(user);
        assertNotEquals(userRepository.findUserByLoginPassword("Login","password"), userOptional, "User is exists.");
    }
}
