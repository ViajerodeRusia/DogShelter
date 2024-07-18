package com.assistance.DogShelter.db.model;

// Модель, представляющая волонтера

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Класс, представляющий волонтера.
 * Содержит информацию о волонтере, такую как имя и номер телефона.
 */
@Entity
@Table(name = "volunteers", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;              // id волонтера
    @Column(name = "chat_id", nullable = false)
    private long chatId;          // id волонтера в Telegram
    @Column(name = "name", nullable = false)
    private String name;          // имя волонтера
    @Column(name = "is_busy", nullable = false)
    @JsonProperty("isBusy")
    private boolean isBusy;       //статус волонтёра (занят)
}
