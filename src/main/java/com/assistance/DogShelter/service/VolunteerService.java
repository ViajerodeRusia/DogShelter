package com.assistance.DogShelter.service;

import com.assistance.DogShelter.exceptions.VolunteerNotFoundException;
import com.assistance.DogShelter.db.model.Volunteer;
import com.assistance.DogShelter.db.repository.VolunteerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class VolunteerService {
    @Autowired
    private VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    /**
     * Метод добавляет волонтера в базу данных.
     *
     * @param volunteer
     * @return Volunteer
     */
    public Volunteer addVolunteer(Volunteer volunteer) {
        volunteer.setBusy(false);
        return volunteerRepository.save(volunteer);
    }

    /**
     * Метод ищет волонтера по уникальному идентификатору в базе данных.
     *
     * @param id
     * @return Optional<Volunteer>
     */
    public Optional<Volunteer> findVolunteerById(Long id) {
        return volunteerRepository.findById(id);
    }

    /**
     * Метод ищет волонтера по уникальному идентификатору Телеграм в базе данных.
     *
     * @param chatId
     * @return Optional<Volunteer>
     */
    public Optional<Volunteer> findVolunteerByChatId(Long chatId) {
        return volunteerRepository.findByChatId(chatId);
    }

    /**
     * Метод ищет свободного волонтера в базе данных.
     *
     * @return Optional<Volunteer>
     */
    public Optional<Volunteer> findFreeVolunteer() {
        return volunteerRepository.findFirstByIsBusy(false);
    }

    /**
     * Метод ищет волонтера по уникальному идентификатору Телеграм в базе данных
     * и меняет его статус занятости (false, true)
     *
     * @param chatId
     * @param isBusy
     * @return Volunteer
     * @throws VolunteerNotFoundException если волонтер не был найден
     */
    public Volunteer keepVolunteerBusy(Long chatId, Boolean isBusy) {
        Optional<Volunteer> volunteerOptional = findVolunteerByChatId(chatId);
        if (volunteerOptional.isEmpty()) {
            throw new VolunteerNotFoundException();
        }
        Volunteer volunteer = volunteerOptional.get();
        volunteer.setBusy(isBusy);

        return editVolunteer(volunteer);
    }

    /**
     * Метод редактирует волонтера в базе данных.
     *
     * @param volunteer
     * @return Volunteer
     */
    public Volunteer editVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    /**
     * Метод удаляет волонтера по уникальному идентификатору из базы данных.
     *
     * @param id
     */
    public void deleteVolunteer(Long id) {
        volunteerRepository.deleteById(id);
    }

    /**
     * Метод удаляет волонтера по уникальному идентификатору Телеграм из базы данных.
     *
     * @param chatId
     */
    public void deleteVolunteerChatId(Long chatId) {
        var id = volunteerRepository.findByChatId(chatId).get().getId();
        volunteerRepository.deleteById(id);
    }

    /**
     * Метод выводит список всех зарегистрированных волонтёров.
     *
     * @return Collection
     */
    public Collection<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }
}
