package com.assistance.DogShelter.mapper;

import com.assistance.DogShelter.controller.dto.PetDto;
import com.assistance.DogShelter.db.model.Pet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PetMapper {

    public PetDto mapToPetDto(Pet pet) {
        return PetDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(pet.getBreed())
                .age(pet.getAge())
                .food(pet.getFood())
                .user(pet.getUser())
                .adoptionDate(pet.getDateAdoption())
                .build();
    }

    public List<PetDto> mapToPetDtos(List<Pet> pets) {
        return pets.stream()
                .map(this::mapToPetDto)
                .toList();
    }

    public Pet mapToPet(PetDto petDto) {
        Pet pet = new Pet();
        pet.setId(petDto.getId());
        pet.setName(petDto.getName());
        pet.setBreed(petDto.getBreed());
        pet.setAge(petDto.getAge());
        pet.setFood(petDto.getFood());
        pet.setUser(petDto.getUser());
        pet.setDateAdoption(petDto.getAdoptionDate());
        return pet;
    }
}
