package com.assistance.DogShelter.service;

import com.assistance.DogShelter.controller.dto.PetDto;
import com.assistance.DogShelter.db.model.Pet;
import com.assistance.DogShelter.db.repository.PetRepository;
import com.assistance.DogShelter.mapper.PetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для работы с питомцами.
 */
@Service
public class PetService {
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    @Autowired
    public PetService(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }


    /**
     * Добавляет нового питомца.
     *
     * @param petDto Питомец для добавления.
     * @return Добавленный питомец.
     */
    public PetDto addPet(PetDto petDto) {
        Pet pet = petMapper.mapToPet(petDto);
        System.out.println("Mapped Pet: " + pet);
        Pet savedPet = petRepository.save(pet);
        System.out.println("Saved Pet: " + savedPet);
        return petMapper.mapToPetDto(savedPet);
    }

    /**
     * Находит питомца по идентификатору.
     *
     * @param id Идентификатор питомца.
     * @return Питомец с указанным идентификатором, если найден.
     */
    public Optional<PetDto> findPetById(Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        return pet.map(petMapper::mapToPetDto);
    }

    /**
     * Обновляет данные питомца.
     *
     * @param petDto Питомец с обновленными данными.
     * @return Обновленный питомец.
     */
    public PetDto editPet(PetDto petDto) {
        Pet pet = petMapper.mapToPet(petDto);
        Pet updatedPet = petRepository.save(pet);
        return petMapper.mapToPetDto(updatedPet);
    }

    /**
     * Удаляет питомца по идентификатору.
     *
     * @param id Идентификатор питомца для удаления.
     */
    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    /**
     * Возвращает список всех питомцев.
     *
     * @return Список всех питомцев.
     */
    public Collection<PetDto> getAllPets() {
        return petRepository.findAll().stream()
                .map(petMapper::mapToPetDto)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список питомцев по идентификатору приюта.
     *
     * @param shelterId Идентификатор приюта.
     * @return Список питомцев приюта.
     */
    public Collection<PetDto> getPetsByShelterId(Long shelterId) {
        return petRepository.findByShelterId(shelterId).stream()
                .map(petMapper::mapToPetDto)
                .collect(Collectors.toList());
    }
}