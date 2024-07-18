package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.model.User;
import com.assistance.DogShelter.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Метод добавляет пользователя в базу данных.
     *
     * @param user
     * @return user
     */
    public User addUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Метод ищет пользователя по уникальному идентификатору в базе данных.
     *
     * @param id
     * @return Optional<User>
     */
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Метод ищет пользователя по уникальному идентификатору Телеграм в базе данных.
     *
     * @param chatId
     * @return Optional<User>
     */
    public Optional<User> findUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    /**
     * Метод редактирует пользователя в базе данных.
     *
     * @param user
     * @return user
     */
    public User editUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Метод удаляет пользователя по уникальному идентификатору из базы данных.
     *
     * @param id
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Метод удаляет пользователя по уникальному идентификатору Телеграм из базы данных.
     *
     * @param chatId
     */
    public void deleteUserChatId(Long chatId) {
        var id = userRepository.findByChatId(chatId).get().getChatId();
        userRepository.deleteById(id);
    }

    /**
     * Метод выводит список всех зарегистрированных пользователей.
     *
     * @return Collection
     */
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

}