package com.assistance.DogShelter.db.repository;

import com.assistance.DogShelter.db.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с приютами.
 */
@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    List<Shelter> findAll();
}
