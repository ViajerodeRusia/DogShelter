package com.assistance.DogShelter.service;

import com.assistance.DogShelter.controller.dto.PetDto;
import com.assistance.DogShelter.db.entity.Pet;
import com.assistance.DogShelter.db.repository.PetRepository;
import com.assistance.DogShelter.mapper.PetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JUnitPetServiceTest {

    private PetRepository petRepository;
    private PetMapper petMapper;
    private PetService petService;

    @BeforeEach
    void setUp() {
        petRepository = mock(PetRepository.class);
        petMapper = mock(PetMapper.class);
        petService = new PetService(petRepository, petMapper);
    }

    @Test
    void addPet() {
        PetDto petDto = new PetDto(1L, "Бобик", "Собака", 3, "Корм", null, null);
        Pet pet = new Pet(1L, "Бобик", "Собака", 3, "Корм", null, null, null);

        when(petMapper.mapToPet(petDto)).thenReturn(pet);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(petMapper.mapToPetDto(pet)).thenReturn(petDto);

        PetDto result = petService.addPet(petDto);

        assertNotNull(result);
        Assertions.assertEquals(petDto.getName(), result.getName());
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    void findPetById() {
        Pet pet = new Pet(1L, "Бобик", "Собака", 3, "Корм", null, null, null);
        PetDto petDto = new PetDto(1L, "Бобик", "Собака", 3, "Корм", null, null);

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petMapper.mapToPetDto(pet)).thenReturn(petDto);

        Optional<PetDto> result = petService.findPetById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(petDto.getName(), result.get().getName());
    }

    @Test
    void editPet() {
        PetDto petDto = new PetDto(1L, "Бобик", "Собака", 3, "Корм", null, LocalDate.now());
        Pet pet = new Pet(1L, "Бобик", "Собака", 3, "Корм", null, null, LocalDate.now());

        when(petMapper.mapToPet(petDto)).thenReturn(pet);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(petMapper.mapToPetDto(pet)).thenReturn(petDto);

        PetDto result = petService.editPet(petDto);

        assertNotNull(result);
        Assertions.assertEquals(petDto.getName(), result.getName());
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    void deletePet() {
        Long petId = 1L;
        petService.deletePet(petId);

        verify(petRepository, times(1)).deleteById(petId);
    }

    @Test
    void getAllPets() {
        Pet pet = new Pet(1L, "Бобик", "Собака", 3, "Корм", null, null, null);
        PetDto petDto = new PetDto(1L, "Бобик", "Собака", 3, "Корм", null, null);

        when(petRepository.findAll()).thenReturn(List.of(pet));
        when(petMapper.mapToPetDto(pet)).thenReturn(petDto);

        Collection<PetDto> result = petService.getAllPets();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(petDto.getName(), result.iterator().next().getName());
    }

    @Test
    void getPetsByShelterId() {
        Long shelterId = 1L;
        Pet pet = new Pet(1L, "Бобик", "Собака", 3, "Корм", null, null, null);
        PetDto petDto = new PetDto(1L, "Бобик", "Собака", 3, "Корм", null, null);

        when(petRepository.findByShelterId(shelterId)).thenReturn(List.of(pet));
        when(petMapper.mapToPetDto(pet)).thenReturn(petDto);

        Collection<PetDto> result = petService.getPetsByShelterId(shelterId);

        assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(petDto.getName(), result.iterator().next().getName());
    }
}