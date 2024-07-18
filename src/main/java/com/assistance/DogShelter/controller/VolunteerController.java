package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.db.model.Volunteer;
import com.assistance.DogShelter.service.VolunteerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

/**
 * Контроллер для обработки HTTP-запросов, связанных с питомцами.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/volunteers")
@Slf4j
public class VolunteerController {


    private final VolunteerService volunteerService;

    /**
     * Конструктор контроллера, который принимает сервис для работы с волонтерами.
     *
     * @param volunteersService Сервис для работы с волонтерами.
     */
    public VolunteerController(VolunteerService volunteersService) {
        this.volunteerService = volunteersService;
    }

    /**
     * Обрабатывает POST-запрос для добавления нового волонтера.
     *
     * @param volunteer Переданный волонтер в теле запроса.
     * @return волонтер, который был добавлен с помощью сервиса.
     */
    @PostMapping("/add")
    @Operation(summary = "Add volunteer",
            description = "Adds a new volunteer to the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public ResponseEntity<Volunteer> addVolunteer(@RequestBody Volunteer volunteer) {
        Volunteer addedVolunteer = volunteerService.addVolunteer(volunteer);
        log.info("Added Volunteer: {}", addedVolunteer); // Логгирование добавленного волонтера
        return ResponseEntity.status(HttpStatus.CREATED).body(addedVolunteer);
    }

    /**
     * Обрабатывает GET-запрос для поиска волонтера по идентификатору.
     *
     * @param id Идентификатор волонтера.
     * @return волонтер с указанным идентификатором, если найден, в противном случае возвращает 404 ошибку.
     */
    @GetMapping("{id}")
    @Operation(summary = "Search volunteer",
            description = "Searches for a volunteer in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Volunteer not found")
            })
    public ResponseEntity<Volunteer> findVolunteerById(@PathVariable Long id) {
        Optional<Volunteer> volunteer = volunteerService.findVolunteerById(id);
        return volunteer.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Обрабатывает PUT-зарос для редактирования волонтера.
     *
     * @param volunteer волонтерами с обновленными данными в теле запроса.
     * @param id        Идентификатор редактируемого волонтера.
     * @return Обновленный волонтер, если редактирование выполнено успешно, в противном случае возвращает 404 ошибку.
     */
    @PutMapping("{id}")
    @Operation(summary = "Update volunteer",
            description = "Updates a volunteer's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Volunteer not found")
            })
    public ResponseEntity<Volunteer> editVolunteer(@RequestBody Volunteer volunteer, @PathVariable Long id) {
        Optional<Volunteer> foundVolunteer = volunteerService.findVolunteerById(id);
        if (foundVolunteer.isPresent()) {
            Volunteer updatedVolunteer = volunteerService.editVolunteer(volunteer);
            return ResponseEntity.status(HttpStatus.OK).body(updatedVolunteer);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает POST-запрос для изменения статуса занятости волонтера
     *
     * @param userId Идентификатор волонтера для установления занятости
     * @param busy   Идентификатор для установки занятости (<code>true</code> - занят, <code>false</code> - не занят)
     * @return Подтверждение успешного изменения статуса занятости волонтера
     */
    @PostMapping("{userId}")
    @Operation(summary = "Set volunteer",
            description = "Set status for a volunteer as busy",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Volunteer not found")
            })
    public ResponseEntity<Volunteer> setVolunteerBusy(@PathVariable Long userId, @RequestParam Boolean busy) {
        Optional<Volunteer> volunteer = volunteerService.findVolunteerById(userId);
        if (volunteer.isPresent()) {
            Volunteer updatedVolunteer = volunteerService.keepVolunteerBusy(userId, busy);
            return ResponseEntity.status(HttpStatus.OK).body(updatedVolunteer);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления волонтера
     *
     * @param id Идентификатор волонтера для удаления
     * @return Подтверждение успешного удаления волонтера
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Remove volunteer",
            description = "Removes a volunteer from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Volunteer not found")
            })
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Long id) {
        if (volunteerService.findVolunteerById(id).isPresent()) {
            volunteerService.deleteVolunteer(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления волонтера по Телеграм ID
     *
     * @param chatId Идентификатор Телеграм волонтера для удаления
     * @return Подтверждение успешного удаления волонтера
     */
    @DeleteMapping("/chatId/{chatId}")
    @Operation(summary = "Remove volunteer by Telegram ID",
            description = "Removes a volunteer by Telegram ID from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Volunteer not found")
            })
    public ResponseEntity<Void> deleteVolunteerChatId(@PathVariable Long chatId) {
        if (volunteerService.findVolunteerByChatId(chatId).isPresent()) {
            volunteerService.deleteVolunteerChatId(chatId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/all")
    @Operation(summary = "Get all volunteers",
            description = "Retrieves a list of all volunteers in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public ResponseEntity<Collection<Volunteer>> getAllVolunteers() {
        return ResponseEntity.ok(volunteerService.getAllVolunteers());
    }

}
