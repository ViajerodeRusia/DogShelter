package com.assistance.DogShelter.db.model;

// Модель, представляющая пользователя

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.glassfish.jersey.spi.Contract;

import java.util.List;
import java.util.Set;

/**
 * Модель, представляющая пользователя.
 */
@Entity
@Table(name = "users", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "pets"})

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;              // id пользователя

    @Column(name = "chat_id", nullable = false)
    private long chatId;          // id пользователя в Telegram

    @Column(name = "name", nullable = false)
    private String name;          // имя пользователя

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;   // номер телефона пользователя

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Pet> pets;        // связать с классом Pet

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Report> reports; //связать с классом Report

    @Column(name = "extension", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean extension;       //Статус для отчетности. Срок продлен или нет.
}
