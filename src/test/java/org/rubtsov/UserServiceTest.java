package org.rubtsov;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.rubtsov.exception.UserNonUniqueException;
import org.rubtsov.model.User;
import org.rubtsov.service.UserRepository;
import org.rubtsov.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private User user;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    //--------------Tests for getAllLogins() ---------------------------------------------------------------------------
    @Test
    @DisplayName("1.1 Проверяем, что метод getAllLogins создает корректный список логинов пользователей")
    public void getAllLoginsReturnsCorrectUserList() {
        when(userRepository.getAllUsers())
                .thenReturn(List.of(new User("User1", "pass1"),
                        new User("user2", "pass2")));
        assertThat(userService.getListAllLogins()).isEqualTo(List.of("User1", "user2"));
    }
    //--------------Tests for creatNewUser() ------------------------------------------------------------------------------------
    @Test
    @DisplayName("2.1 Добавление пользователя с пустым логином бросает исключение и метод creatNewUser() не вызывается")
    public void addingUserWithEmptyLoginThrowsException() {
        assertThatThrownBy(() -> userService.creatNewUser("", "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Login should be defined");
        verify(userRepository, never()).addUser(any(User.class));
    }
    @Test
    @DisplayName("2.2 Добавление пользователя с логином null бросает исключение и метод creatNewUser() не вызывается")
    public void addingUserWithNullLoginThrowsException() {
        assertThatThrownBy(() -> userService.creatNewUser(null, "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Login should be defined");
        verify(userRepository, never()).addUser(any(User.class));
    }
    @Test
    @DisplayName("2.3 Добавление пользователя с пустым паролем бросает исключение,  метод creatNewUser() не вызывается")
    public void addingUserWithEmptyPasswordThrowsException() {
        assertThatThrownBy(() -> userService.creatNewUser("login", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password should be defined");
        verify(userRepository, never()).addUser(any(User.class));
    }
    @Test
    @DisplayName("2.4 Добавление пользователя с паролем null бросает исключение,  метод creatNewUser() не вызывается")
    public void addingUserWithNullPasswordThrowsException() {
        assertThatThrownBy(() -> userService.creatNewUser("login", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password should be defined");
        verify(userRepository, never()).addUser(any(User.class));
    }
    @Test
    @DisplayName("2.5 Добавление уже существующего пользователя бросает исключение, метод creatNewUser() не вызывается")
    public void whenAddNotUniqueUserThrowsException() {
        when(userRepository.getAllUsers()).thenReturn(List.of(new User("login", "password")));
        assertThatThrownBy(() -> userService.creatNewUser("login", "password"))
                .isInstanceOf(UserNonUniqueException.class).hasMessage("User already exists");
        verify(userRepository, never()).addUser(any(User.class));
    }
    @Test
    @DisplayName("2.6 При создании нового уникального пользователя вызывается метод creatNewUser()")
    void whenUniqueUserIsAddedThenCreatNewUserIsCalled() {
        when(userRepository.getAllUsers()).thenReturn(List.of());
        userService.creatNewUser("login", "password");
        verify(userRepository).addUser(any());
    }
    //-------------Tests for userWithSuchLoginPasswordIsExists() ----------------------------------------------------------------
    @Test
    @DisplayName("3.1 При поиске пользователя по невалидным логину и паролю метод userWithSuchLoginPasswordIsExists() " +
            "возвращает false и метод вызывается не более 1 раза")
    void whenSearchingUserByNotValiLoginPasswordReturnFalse() {
        when(userRepository.findUserByLoginPassword(anyString(), anyString())).thenReturn(Optional.empty());
        assertThat(userService.userWithSuchLoginPasswordIsExists(anyString(), anyString())).isFalse();
        verify(userRepository, atMostOnce()).findUserByLoginPassword(anyString(), anyString());
    }
    @Test
    @DisplayName("3.2 При поиске пользователя по валилным логину и паролю метод userWithSuchLoginPasswordIsExists() " +
            "возвращает true и  метод вызывается не более 1 раза")
    void whenSearchingUserByValidLoginPasswordThenReturnTrue() {
        when(userRepository.findUserByLoginPassword("user", "pass")).thenReturn(Optional.of(new User("user", "pass")));
        assertThat(userService.userWithSuchLoginPasswordIsExists("user", "pass")).isTrue();
        verify(userRepository, atMostOnce()).findUserByLoginPassword(anyString(), anyString());
    }

}