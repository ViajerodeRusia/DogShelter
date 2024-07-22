package com.assistance.DogShelter.controller.dto;

import com.assistance.DogShelter.db.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDto {
    private long id;                // id питомца
    private String name;            // имя питомца
    private String breed;           // порода питомца
    private int age;                // возраст питомца
    private String food;            // предпочитаемая еда питомца
    private User user;              // связать с классом User
    private LocalDate adoptionDate; // дата усыновления
    // убрали shelter, не хотим рекурсию
}
