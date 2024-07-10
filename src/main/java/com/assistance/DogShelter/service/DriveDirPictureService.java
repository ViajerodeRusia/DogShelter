package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.model.DriveDirPicture;
import com.assistance.DogShelter.db.model.Shelter;
import com.assistance.DogShelter.db.repository.DriveDirPictureRepository;
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

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис для работы с изображениями, загруженными в хранилище.
 */
@Service
@Transactional
public class DriveDirPictureService {
    @Value("/driveDirPictures")
    private String drivePictureDir; //Название папки, где будем хранить обложки
    private final DriveDirPictureRepository driveDirPictureRepository;
    private final ShelterService shelterService;

    @Autowired
    public DriveDirPictureService(DriveDirPictureRepository driveDirPictureRepository, ShelterService shelterService) {
        this.driveDirPictureRepository = driveDirPictureRepository;
        this.shelterService = shelterService;
    }

    public void uploadCover(Long shelterId, MultipartFile file) throws IOException {
        Shelter shelter = shelterService.findShelterById(shelterId);
        Path filePath = Path.of(drivePictureDir,shelterId + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try(InputStream is = file.getInputStream();
            OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        DriveDirPicture driveDirPicture = findPicture(shelterId);
        driveDirPicture.setShelter(shelter);
        driveDirPicture.setFilePath(filePath.toString());
        driveDirPicture.setFileSize(file.getSize());
        driveDirPicture.setMediaType(file.getContentType());
        driveDirPicture.setData(generateImagePreview(filePath));
        driveDirPictureRepository.save(driveDirPicture);
    }
    public DriveDirPicture findPicture(Long shelterId) {
        return driveDirPictureRepository.findByShelterId(shelterId).orElse(new DriveDirPicture());
    }
    private byte[] generateImagePreview(Path filePath) throws IOException {
        try(InputStream is = Files.newInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight()/(image.getWidth()/100);
            BufferedImage preview =  new BufferedImage(100, height, image.getType());
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
