package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.entity.User;
import com.assistance.DogShelter.db.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JUnitUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() {
        User user = new User(1L, 123L, "Daniel", "8-800-600-30-20", null, null, false);

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.addUser(user);
        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserById() {
        User user = new User(1L, 123L, "Daniel", "8-800-600-30-20", null, null, false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(1L);

        if(result.isPresent()) {
            Assertions.assertEquals(user, result.get());
            verify(userRepository, times(1)).findById(1L);
        }
    }

    @Test
    void testFindUserByChatId() {
        User user = new User(1L, 123L, "Daniel", "8-800-600-30-20", null, null, false);

        when(userRepository.findByChatId(123L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserByChatId(123L);
        Assertions.assertTrue(result.isPresent());
        verify(userRepository, times(1)).findByChatId(123L);
    }

    @Test
    void testEditUser() {
        User user = new User(1L, 123L, "Daniel", "8-800-600-30-20", null, null, false);

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.editUser(user);
        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserChatId() {
        User user = new User(1L, 123L, "Daniel", "8-800-600-30-20", null, null, false);

        when(userRepository.findByChatId(123L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(user.getChatId());

        userService.deleteUserChatId(123L);
        verify(userRepository, times(1)).findByChatId(123L);
        verify(userRepository, times(1)).deleteById(user.getChatId());
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User(1L, 123L, "Daniel", "8-800-600-30-20", null, null, false);
        User user2 = new User(2L, 321L, "Jordan", "8-800-600-20-30", null, null, false);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        var result = userService.getAllUsers();
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }
}