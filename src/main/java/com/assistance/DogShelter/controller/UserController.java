package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.db.entity.User;
import com.assistance.DogShelter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

/**
 * Контроллер для обработки HTTP-запросов, связанных с пользователем.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Конструктор контроллера, который принимает сервис для работы с пользователем.
     *
     * @param userService Сервис для работы с пользователем.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обрабатывает POST-запрос для добавления нового пользователя.
     *
     * @param user Переданный пользователь в теле запроса.
     * @return Пользователь, который был добавлен с помощью сервиса.
     */
    @PostMapping("/add")
    @Operation(summary = "Add user",
            description = "Adds a new user to the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User addUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(addUser);
    }

    /**
     * Обрабатывает GET-запрос для поиска пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Пользователь с указанным идентификатором, если найден, в противном случае возвращает 404 ошибку.
     */
    @GetMapping("{id}")
    @Operation(summary = "Search user",
            description = "Searches for a user in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Обрабатывает PUT-зарос для редактирования пользователя.
     *
     * @param user Пользователь с обновленными данными в теле запроса.
     * @param id   Идентификатор редактируемого пользователя.
     * @return Обновленный пользователь, если редактирование выполнено успешно, в противном случаи возвращает 404 ошибку.
     */
    @PutMapping("{id}")
    @Operation(summary = "Update user",
            description = "Updates a user's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public ResponseEntity<User> editUser(@RequestBody User user, @PathVariable Long id) {
        Optional<User> foundUser = userService.findUserById(id);
        if (foundUser.isPresent()) {
            User updatedUser = userService.editUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя для удаления.
     * @return Подтверждение успешного удаления пользователя.
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Remove user",
            description = "Removes a user from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.findUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления пользователя по Телеграм ID
     *
     * @param chatId Идентификатор Телеграм пользователя для удаления
     * @return Подтверждение успешного удаления волонтера
     */
    @DeleteMapping("/chatId/{chatId}")
    @Operation(summary = "Remove user by Telegram ID",
            description = "Removes a user by Telegram ID from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public ResponseEntity<Void> deleteVolunteerChatId(@PathVariable Long chatId) {
        if (userService.findUserByChatId(chatId).isPresent()) {
            userService.deleteUserChatId(chatId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/all")
    @Operation(summary = "Get all users",
            description = "Retrieves a list of all users in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public ResponseEntity<Collection<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
