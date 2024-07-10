package com.assistance.DogShelter.controller.dto;

import com.assistance.DogShelter.db.model.Shelter;
import com.assistance.DogShelter.db.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PetDto {
    private long id;            // id питомца
    private String name;        // имя питомца
    private String breed;       // порода питомца
    private int age;            // возраст питомца
    private String food;        // предпочитаемая еда питомца
    private User user;          // связать с классом User
    private Shelter shelter;    // убрали Shelter, не хотим рекурсию
}
