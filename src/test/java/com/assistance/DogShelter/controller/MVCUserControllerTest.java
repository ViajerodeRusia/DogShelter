package com.assistance.DogShelter.controller;


import com.assistance.DogShelter.db.entity.User;
import com.assistance.DogShelter.service.UserService;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class MVCUserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void getUserByIdTest() throws Exception {
        Long id = 1L;
        String name = "Иван Иванов";
        Long chatId = 123456789L;
        String phoneNumber = "1234567890";

        User expectedUser = new User();
        expectedUser.setId(id);
        expectedUser.setName(name);
        expectedUser.setChatId(chatId);
        expectedUser.setPhoneNumber(phoneNumber);

        when(userService.findUserById(id)).thenReturn(Optional.of(expectedUser));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{id}", id)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chatId").value(chatId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(phoneNumber));
    }

    @Test
    void getAllUsersTest() throws Exception {
        // Создаем и сохраняем пользователей в сервисе или базе данных
        List<User> users = Arrays.asList(
                new User(1L, 12345778L, "Иван Иванов", "8-800-600-30-05", null, null, false),
                new User(2L, 12435778L, "Петр Петров", "8-800-500-30-05", null, null, false)
        );
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users/all")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2))); // Проверяем, что вернулась коллекция из двух пользователей
    }

    @Test
    public void createUserTest() throws Exception {
        Long id = 1L;
        String name = "Иван Иванов";
        Long chatId = 123456789L;
        String phoneNumber = "1234567890";

        JSONObject userObject = new JSONObject();
        userObject.put("name", name);
        userObject.put("chatId", chatId);
        userObject.put("phoneNumber", phoneNumber);

        User newUser = new User();
        newUser.setId(id);
        newUser.setName(name);
        newUser.setChatId(chatId);
        newUser.setPhoneNumber(phoneNumber);

        when(userService.addUser(any(User.class))).thenReturn(newUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/add")
                        .content(userObject.toString())
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chatId").value(chatId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(phoneNumber));
    }

    @Test
    public void deleteUserTest() throws Exception {
        Long id = 1L;

        // Настройка моков
        User mockUser = new User();
        mockUser.setId(id);
        when(userService.findUserById(id)).thenReturn(Optional.of(mockUser));
        doNothing().when(userService).deleteUser(id);

        // Вызов и проверка
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/{id}", id)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserByChatIdTest() throws Exception {
        Long chatId = 123456789L;

        // Настройка моков
        User mockUser = new User();
        mockUser.setChatId(chatId);
        when(userService.findUserByChatId(chatId)).thenReturn(Optional.of(mockUser));
        doNothing().when(userService).deleteUserChatId(chatId);

        // Вызов и проверка
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/chatId/{chatId}", chatId)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void editUserTest() throws Exception {
        Long id = 1L;
        String name = "Иван Иванов";
        Long chatId = 123456789L;
        String phoneNumber = "1234567890";

        JSONObject userObject = new JSONObject();
        userObject.put("name", name);
        userObject.put("chatId", chatId);
        userObject.put("phoneNumber", phoneNumber);

        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setName(name);
        updatedUser.setChatId(chatId);
        updatedUser.setPhoneNumber(phoneNumber);

        when(userService.findUserById(id)).thenReturn(Optional.of(updatedUser));
        when(userService.editUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/{id}", id)
                        .content(userObject.toString())
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.chatId").value(chatId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(phoneNumber));
    }
}