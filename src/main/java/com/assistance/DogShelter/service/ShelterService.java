package com.assistance.DogShelter.service;

import com.assistance.DogShelter.controller.dto.ShelterDto;
import com.assistance.DogShelter.db.model.Pet;
import com.assistance.DogShelter.db.model.Shelter;
import com.assistance.DogShelter.db.repository.ShelterRepository;
import com.assistance.DogShelter.mapper.ShelterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для работы с приютами.
 */
@Service
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final ShelterMapper shelterMapper;
    @Autowired
    public ShelterService(ShelterRepository shelterRepository, ShelterMapper shelterMapper) {
        this.shelterRepository = shelterRepository;
        this.shelterMapper = shelterMapper;
    }


    /**
     * Добавляет новый приют.
     *
     * @param shelterDto Приют для добавления.
     * @return Добавленный приют.
     */
    public ShelterDto addShelter(ShelterDto shelterDto) {
        Shelter shelter = shelterMapper.mapToShelter(shelterDto);
        Shelter savedShelter = shelterRepository.save(shelter);
        return shelterMapper.mapToShelterDto(savedShelter);
    }

    /**
     * Находит приют по идентификатору.
     *
     * @param id Идентификатор приюта.
     * @return Приют с указанным идентификатором, если найден.
     */
    public Optional<ShelterDto> findShelterById(Long id) {
        Optional<Shelter> shelter= shelterRepository.findById(id);
        return shelter.map(shelterMapper::mapToShelterDto);
    }

    /**
     * Обновляет данные приюта.
     *
     * @param shelterDto Приют с обновленными данными.
     * @return Обновленный приют.
     */
    public ShelterDto editShelter(ShelterDto shelterDto) {
        Shelter shelter = shelterMapper.mapToShelter(shelterDto);
        Shelter updatedShelter = shelterRepository.save(shelter);
        return shelterMapper.mapToShelterDto(updatedShelter);
    }

    /**
     * Удаляет приют по идентификатору.
     *
     * @param id Идентификатор приюта для удаления.
     */
    public void deleteShelter(Long id) {
        shelterRepository.deleteById(id);
    }

    /**
     * Возвращает список всех приютов.
     *
     * @return Список всех приютов.
     */
    public List<ShelterDto> getAllShelters() {
        return shelterRepository.findAll().stream()
                .map(shelterMapper::mapToShelterDto)
                .collect(Collectors.toList());
    }

}
