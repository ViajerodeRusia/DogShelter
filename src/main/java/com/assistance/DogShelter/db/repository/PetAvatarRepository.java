package com.assistance.DogShelter.db.repository;

import com.assistance.DogShelter.db.model.PetAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с аватарами питомцев.
 */
@Repository
public interface PetAvatarRepository extends JpaRepository<PetAvatar, Long> {
    Optional<PetAvatar> findByPetId(Long shelterId);
}
