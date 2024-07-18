package com.assistance.DogShelter.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Модель, представляющая питомца.
 */
@Entity
@Table(name = "pets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;            // id питомца

    @Column(name = "name", nullable = false)
    private String name;        // имя питомца

    @Column(name = "breed", nullable = false)
    private String breed;       // порода питомца

    @Column(name = "age", nullable = false)
    private int age;            // возраст питомца

    @Column(name = "food")
    private String food;        // предпочитаемая еда питомца

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;          // связать с классом User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;    // связать с классом Shelter

    @Column(name = "date_adoption", nullable = true)       // дата усыновления
    private LocalDate dateAdoption;
}