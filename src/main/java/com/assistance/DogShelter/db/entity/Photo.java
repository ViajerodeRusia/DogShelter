package com.assistance.DogShelter.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fileid", nullable = false)
    private String fileId;
    @Column(name = "filepath", nullable = false)
    private String filePath;
    @Lob
    @Column(name = "data", columnDefinition = "BYTEA")
    private byte[] data; // Поле для хранения изображения
    @OneToOne
    @JoinColumn(name = "report_id", nullable = true)
    Report report;

}
