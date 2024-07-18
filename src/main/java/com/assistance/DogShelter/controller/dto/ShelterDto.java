package com.assistance.DogShelter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShelterDto {
    private long id;
    private String name;
    private String address;
    private List<PetDto> pets;// связать с классом Pet
}
