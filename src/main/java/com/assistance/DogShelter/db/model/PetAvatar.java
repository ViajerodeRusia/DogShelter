package com.assistance.DogShelter.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Модель для представления аватара питомца.
 */
@Entity
@Table(name = "pet_avatars")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetAvatar {
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
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
}
