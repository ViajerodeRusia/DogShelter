package com.assistance.DogShelter.db.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.assistance.DogShelter.db.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищет пользователя по идентификатору чата.
     *
     * @param chatId идентификатор чата
     * @return Optional, содержащий пользователя, если найден
     */
    Optional<User> findByChatId(long chatId);

    Optional<User> findById(long id);
}
