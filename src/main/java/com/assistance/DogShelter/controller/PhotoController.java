package com.assistance.DogShelter.controller;

import com.assistance.DogShelter.db.entity.Photo;
import com.assistance.DogShelter.db.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPhotoById(@PathVariable Long id) {
        Optional<Photo> photo = photoRepository.findById(id);
        if (photo.isPresent()) {
            Photo p = photo.get();
            byte[] image = p.getData();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE) // Или другой тип в зависимости от изображения
                    .body(image);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}