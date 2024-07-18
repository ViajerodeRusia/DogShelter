package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.db.model.DriveDirPicture;
import com.assistance.DogShelter.service.DriveDirPictureService;
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
 * Контроллер для обработки HTTP-запросов, связанных с изображениями, загруженными в хранилище.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/api/dirPictures")
@Tag(name = "DriveDirPicture", description = "API для работы с изображениями")
public class DriveDirPictureController {

    private final DriveDirPictureService driveDirPictureService;

    @Autowired
    public DriveDirPictureController(DriveDirPictureService driveDirPictureService) {
        this.driveDirPictureService = driveDirPictureService;
    }

    @PostMapping(value = "/{id}/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPicture(@PathVariable Long id, @RequestParam MultipartFile cover) throws IOException {
        if(cover.getSize() >= 1024*300) {
            return  ResponseEntity.badRequest().body("File is too big");
        }
        driveDirPictureService.uploadCover(id, cover);
        return  ResponseEntity.ok().build();
    }
    @GetMapping(value = "/{id}/picture/preview")
    public ResponseEntity<byte[]> downloadPicture(@PathVariable Long id) {
        DriveDirPicture driveDirPicture = driveDirPictureService.findPicture(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(driveDirPicture.getMediaType()));
        headers.setContentLength(driveDirPicture.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(driveDirPicture.getData());

    }
    @GetMapping(value = "/{id}/picture")
    public void downloadPicture(@PathVariable Long id, HttpServletResponse response) throws IOException {
        DriveDirPicture driveDirPicture = driveDirPictureService.findPicture(id);

        Path path = Path.of(driveDirPicture.getFilePath());

        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(driveDirPicture.getMediaType());
            response.setContentLength((int) driveDirPicture.getFileSize());
            is.transferTo(os);
        }
    }
}