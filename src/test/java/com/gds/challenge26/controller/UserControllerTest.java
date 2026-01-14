package com.gds.challenge26.controller;

import com.gds.challenge26.model.User;
import com.gds.challenge26.model.UserDto;
import com.gds.challenge26.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserRepository userRepository;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userController = new UserController(userRepository);
    }

    @Test
    void getUsers_shouldReturnUserDtoList() {
        // Arrange
        List<User> users = List.of(
                new User("Alice"),
                new User("Bob"),
                new User("Charlie")
        );

        when(userRepository.findAll()).thenReturn(users);
        // Act
        List<UserDto> result = userController.getUsers();
        // Assert
        assertEquals(3, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());
        assertEquals("Charlie", result.get(2).getName());
        verify(userRepository).findAll();
    }
}

