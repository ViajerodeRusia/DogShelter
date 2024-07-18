package com.assistance.DogShelter.service;

import com.assistance.DogShelter.controller.dto.PetDto;
import com.assistance.DogShelter.db.model.Pet;
import com.assistance.DogShelter.db.model.PetAvatar;
import com.assistance.DogShelter.db.repository.PetAvatarRepository;
import com.assistance.DogShelter.mapper.PetMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис для работы с аватарами питомцев.
 */
@Service
@Transactional
public class PetAvatarService {
    @Value("/petAvatars")
    private String petAvatarDir;
    private final PetAvatarRepository petAvatarRepository;
    private final PetService petService;
    private final PetMapper petMapper;

    @Autowired
    public PetAvatarService(PetAvatarRepository petAvatarRepository, PetService petService, PetMapper petMapper) {
        this.petAvatarRepository = petAvatarRepository;
        this.petService = petService;
        this.petMapper = petMapper;
    }

    public void uploadCover(Long petId, MultipartFile file) throws IOException {
        Optional<PetDto> petDtoOptional = petService.findPetById(petId);
        if (petDtoOptional.isEmpty()) {
            throw new IllegalArgumentException("Pet not found with id: " + petId);
        }

        PetDto petDto = petDtoOptional.get();
        Pet pet = petMapper.mapToPet(petDto); // Преобразование DTO в сущность

        Path filePath = Path.of(petAvatarDir, petId + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }

        PetAvatar petAvatar = findPicture(petId);
        petAvatar.setPet(pet);
        petAvatar.setFilePath(filePath.toString());
        petAvatar.setFileSize(file.getSize());
        petAvatar.setMediaType(file.getContentType());
        petAvatar.setData(generateImagePreview(filePath));
        petAvatarRepository.save(petAvatar);
    }

    public PetAvatar findPicture(Long petId) {
        return petAvatarRepository.findByPetId(petId).orElse(new PetAvatar());
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
