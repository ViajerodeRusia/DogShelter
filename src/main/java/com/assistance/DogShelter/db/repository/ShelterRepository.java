package com.assistance.DogShelter.db.repository;

import com.assistance.DogShelter.db.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с приютами.
 */
@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    List<Shelter> findAll();
    @Query("SELECT s FROM Shelter s LEFT JOIN FETCH s.pets WHERE s.id = :id")
    Optional<Shelter> findByIdWithPets(@Param("id") Long id);
}
