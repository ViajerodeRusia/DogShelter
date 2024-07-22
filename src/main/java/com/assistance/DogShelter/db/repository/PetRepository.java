package com.assistance.DogShelter.db.repository;

import com.assistance.DogShelter.db.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с питомцами.
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByShelterId(Long shelterId);

    List<Pet> findAll();
}
