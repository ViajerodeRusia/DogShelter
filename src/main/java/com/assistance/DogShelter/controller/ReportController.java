package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.db.model.Report;
import com.assistance.DogShelter.db.model.Shelter;
import com.assistance.DogShelter.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Контроллер для обработки HTTP-запросов, связанных с отчетами.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Обрабатывает POST-запрос для добавления нового отчета.
     *
     * @param report Переданный отчет в теле запроса.
     * @return Отчет, который был добавлен с помощью сервиса.
     */
    @PostMapping("/add")
    @Operation(summary = "Add report",
            description = "Adds a new report to the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public ResponseEntity<Report> addReport(@RequestBody Report report) {
        Report addedReport = reportService.addReport(report);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedReport);
    }

    /**
     * Обрабатывает GET-запрос для поиска отчета по идентификатору.
     *
     * @param id Идентификатор отчета.
     * @return Отчет с указанным идентификатором, если найден, в противном случае возвращает 404 ошибку.
     */
    @GetMapping("{id}")
    @Operation(summary = "Search report",
            description = "Searches for a report in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Report not found")
            })
    public ResponseEntity<Report> findReportById(@PathVariable Long id) {
        Report report = reportService.findReportById(id);
        if (report != null) {
            return ResponseEntity.status(HttpStatus.OK).body(report);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Обрабатывает PUT-запрос для редактирования отчета.
     *
     * @param report Отчет с обновленными данными в теле запроса.
     * @param id     Идентификатор редактируемого отчета.
     * @return Обновленный отчет, если редактирование выполнено успешно, в противном случае возвращает 404 ошибку.
     */
    @PutMapping("{id}")
    @Operation(summary = "Update report",
            description = "Updates a report's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Report not found")
            })
    public ResponseEntity<Report> editReport(@RequestBody Report report, @PathVariable Long id) {
        Report foundReport = reportService.findReportById(id);
        if (foundReport != null) {
            Report updatedReport = reportService.editReport(report);
            return ResponseEntity.status(HttpStatus.OK).body(updatedReport);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления отчета по его идентификатору.
     *
     * @param id Идентификатор отчета для удаления.
     * @return Подтверждение успешного удаления отчета.
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Remove report",
            description = "Removes a report from the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Report not found")
            })
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Обрабатывает GET-запрос для получения списка всех отчетов.
     *
     * @return Список всех отчетов в системе.
     */
    @GetMapping("/all")
    @Operation(summary = "List all reports",
            description = "Retrieves a list of all reports in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            })
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }
}
