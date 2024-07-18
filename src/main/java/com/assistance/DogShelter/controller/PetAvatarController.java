package com.assistance.DogShelter.controller;
import com.assistance.DogShelter.db.model.DriveDirPicture;
import com.assistance.DogShelter.db.model.PetAvatar;
import com.assistance.DogShelter.service.PetAvatarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import java.util.Optional;

/**
 * Контроллер для обработки HTTP-запросов, связанных с аватаром питомца.
 * Включает основные CRUD-запросы.
 */
@RestController
@RequestMapping("/api/petAvatars")
@Tag(name = "PetAvatars", description = "API для работы с изображениями")
public class PetAvatarController {

    private final PetAvatarService petAvatarService;
    @Autowired
    public PetAvatarController(PetAvatarService petAvatarService) {
        this.petAvatarService = petAvatarService;
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile cover) throws IOException {
        if(cover.getSize() >= 1024*300) {
            return  ResponseEntity.badRequest().body("File is too big");
        }
        petAvatarService.uploadCover(id, cover);
        return  ResponseEntity.ok().build();
    }
    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        PetAvatar petAvatar = petAvatarService.findPicture(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(petAvatar.getMediaType()));
        headers.setContentLength(petAvatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(petAvatar.getData());

    }
    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        PetAvatar petAvatar = petAvatarService.findPicture(id);

        Path path = Path.of(petAvatar.getFilePath());

        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(petAvatar.getMediaType());
            response.setContentLength((int) petAvatar.getFileSize());
            is.transferTo(os);
        }
    }
}
