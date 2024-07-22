package com.assistance.DogShelter.service;

import com.assistance.DogShelter.controller.dto.ShelterDto;
import com.assistance.DogShelter.db.entity.Shelter;
import com.assistance.DogShelter.db.repository.ShelterRepository;
import com.assistance.DogShelter.mapper.ShelterMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JUnitShelterServiceTest {

    private ShelterRepository shelterRepository;
    private ShelterMapper shelterMapper;
    private ShelterService shelterService;

    @BeforeEach
    void setUp() {
        shelterRepository = mock(ShelterRepository.class);
        shelterMapper = mock(ShelterMapper.class);
        shelterService = new ShelterService(shelterRepository, shelterMapper);
    }

    @Test
    void addShelter() {
        ShelterDto shelterDto = new ShelterDto(1L, "Приют для собак", "Адрес", null);
        Shelter shelter = new Shelter(1L, "Приют для собак", "Адрес", null);

        when(shelterMapper.mapToShelter(shelterDto)).thenReturn(shelter);
        when(shelterRepository.save(any(Shelter.class))).thenReturn(shelter);
        when(shelterMapper.mapToShelterDto(shelter)).thenReturn(shelterDto);

        ShelterDto result = shelterService.addShelter(shelterDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(shelterDto.getName(), result.getName());
        verify(shelterRepository, times(1)).save(shelter);
    }

    @Test
    void findShelterById() {
        Shelter shelter = new Shelter(1L, "Приют для собак", "Адрес", null);
        ShelterDto shelterDto = new ShelterDto(1L, "Приют для собак", "Адрес", null);

        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));
        when(shelterMapper.mapToShelterDto(shelter)).thenReturn(shelterDto);

        Optional<ShelterDto> result = shelterService.findShelterById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(shelterDto.getName(), result.get().getName());
    }

    @Test
    void editShelter() {
        ShelterDto shelterDto = new ShelterDto(1L, "Приют для собак", "Адрес", null);
        Shelter shelter = new Shelter(1L, "Приют для собак", "Адрес", null);

        when(shelterMapper.mapToShelter(shelterDto)).thenReturn(shelter);
        when(shelterRepository.save(any(Shelter.class))).thenReturn(shelter);
        when(shelterMapper.mapToShelterDto(shelter)).thenReturn(shelterDto);

        ShelterDto result = shelterService.editShelter(shelterDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(shelterDto.getName(), result.getName());
        verify(shelterRepository, times(1)).save(shelter);
    }

    @Test
    void deleteShelter() {
        Long shelterId = 1L;
        shelterService.deleteShelter(shelterId);

        verify(shelterRepository, times(1)).deleteById(shelterId);
    }

    @Test
    void getAllShelters() {
        Shelter shelter = new Shelter(1L, "Приют для собак", "Адрес", null);
        ShelterDto shelterDto = new ShelterDto(1L, "Приют для собак", "Адрес", null);

        when(shelterRepository.findAll()).thenReturn(List.of(shelter));
        when(shelterMapper.mapToShelterDto(shelter)).thenReturn(shelterDto);

        Collection<ShelterDto> result = shelterService.getAllShelters();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(shelterDto.getName(), result.iterator().next().getName());
    }
}