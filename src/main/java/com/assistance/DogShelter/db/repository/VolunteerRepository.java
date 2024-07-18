package com.assistance.DogShelter.db.repository;

import com.assistance.DogShelter.db.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    /**
     * Ищет волонтера по идентификатору чата.
     *
     * @param chatId идентификатор чата
     * @return Optional, содержащий волонтера, если найден
     */
    Optional<Volunteer> findByChatId(long chatId);
    Optional<Volunteer> findFirstByIsBusy(boolean bool);

}
