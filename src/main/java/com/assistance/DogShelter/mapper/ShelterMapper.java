package com.assistance.DogShelter.mapper;

import com.assistance.DogShelter.controller.dto.PetDto;
import com.assistance.DogShelter.controller.dto.ShelterDto;
import com.assistance.DogShelter.db.model.Pet;
import com.assistance.DogShelter.db.model.Shelter;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShelterMapper {

    private static PetDto getPetDto(Pet pet) {
        return PetDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(pet.getBreed())
                .age(pet.getAge())
                .food(pet.getFood())
                .build();
    }

    public ShelterDto mapToShelterDto(Shelter shelter) {
        return ShelterDto.builder()
                .id(shelter.getId())
                .name(shelter.getName())
                .address(shelter.getAddress())
                .pets(shelter.getPets().stream()
                        .map(pet -> getPetDto(pet))
                        .toList()
                )
                .build();
    }

    public List<ShelterDto> mapToShelterDtos(List<Shelter> shelters) {
        return shelters.stream()
                .map(this::mapToShelterDto)
                .collect(Collectors.toList());
    }

    public Shelter mapToShelter(ShelterDto shelterDto) {
        Shelter shelter = new Shelter();
        shelter.setId(shelterDto.getId());
        shelter.setName(shelterDto.getName());
        shelter.setAddress(shelterDto.getAddress());
        return shelter;
    }
}
