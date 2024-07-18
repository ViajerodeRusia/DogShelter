package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.db.model.PhotoReport;
import com.assistance.DogShelter.service.PhotoReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Контроллер для обработки HTTP-запросов, связанных с фото отчетами.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/api/photoReports")
@Tag(name = "PhotoReport", description = "API для работы с фото отчетами")
public class PhotoReportController {

    private final PhotoReportService photoReportService;

    @Autowired
    public PhotoReportController(PhotoReportService photoReportService) {
        this.photoReportService = photoReportService;
    }

    @PostMapping(value = "/{id}/report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadReport(@PathVariable Long id, @RequestParam MultipartFile report) throws IOException {
        if(report.getSize() >= 1024*300) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        photoReportService.uploadCover(id, report);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/report/preview")
    public ResponseEntity<byte[]> downloadReportPreview(@PathVariable Long id) {
        PhotoReport photoReport = photoReportService.findPicture(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(photoReport.getMediaType()));
        headers.setContentLength(photoReport.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(photoReport.getData());
    }

    @GetMapping(value = "/{id}/report")
    public void downloadReport(@PathVariable Long id, HttpServletResponse response) throws IOException {
        PhotoReport photoReport = photoReportService.findPicture(id);

        Path path = Path.of(photoReport.getFilePath());

        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(photoReport.getMediaType());
            response.setContentLength((int) photoReport.getFileSize());
            is.transferTo(os);
        }
    }
}