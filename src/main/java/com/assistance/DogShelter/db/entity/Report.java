package com.assistance.DogShelter.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

/**
 * Модель, представляющая отчет.
 */
@Entity
@Table(name = "reports")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;                // id отчета

    @Column(nullable = false)
    private String text;            // текст отчета

    @Column(nullable = false)       // дата отчета
    private LocalDate date;

    @ColumnDefault("false")      // статус отчета
    private Boolean checkReport;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;              //привязываем отчет к пользователю(User)
}
