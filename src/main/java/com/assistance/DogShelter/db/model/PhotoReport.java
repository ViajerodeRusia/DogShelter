package com.assistance.DogShelter.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Модель для представления фото отчета.
 */
@Entity
@Table(name = "photo_reports")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;                //id фото отчета

    @Column(nullable = false)
    private String filePath;        //путь к файлу с фото отчета

    @Column(nullable = false)
    private long fileSize;          //размер файла

    @Column(nullable = false)
    private String mediaType;       //тип файла

    @Lob
    @Column(nullable = false)
    private byte[] data;            //данные изображения в виде массива байт

    @OneToOne
    @JoinColumn(name = "report_id", nullable = false)
    Report report;                  //привязываем класс PhotoReport к классу Report
}
