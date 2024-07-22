package com.assistance.DogShelter.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Модель для представления изображений, загруженных в хранилище.
 */
@Entity
@Table(name = "drive_dir_pictures")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriveDirPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;

    /**
     * Путь к файлу изображения.
     */
    @Column(nullable = false)
    private String filePath;

    /**
     * Размер файла изображения.
     */
    @Column(nullable = false)
    private long fileSize;

    /**
     * Тип медиа файла изображения.
     */
    @Column(nullable = false)
    private String mediaType;

    /**
     * Данные изображения в виде массива байт.
     */
    @Lob
    @Column(nullable = false)
    private byte[] data;
    @OneToOne
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;
}
