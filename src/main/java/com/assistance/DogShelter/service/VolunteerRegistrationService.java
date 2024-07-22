package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.entity.Volunteer;
import com.assistance.DogShelter.db.repository.VolunteerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class VolunteerRegistrationService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private ApplicationContext applicationContext;

    private Map<Long, Boolean> awaitingName = new HashMap<>();

    public void registerVolunteer(Update update) {
        long chatId = update.getMessage().getChatId();
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);
        if (volunteerRepository.findByChatId(chatId).isPresent()) {
            bot.sendMessage(chatId, "Вы уже зарегистрированы как волонтер.");
        } else {
            askForName(chatId);
        }
    }

    public void registerVolunteer(long chatId) {
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);
        if (volunteerRepository.findByChatId(chatId).isPresent()) {
            bot.sendMessage(chatId, "Вы уже зарегистрированы как волонтер.");
        } else {
            askForName(chatId);
        }
    }

    private void askForName(long chatId) {
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);
        bot.sendMessage(chatId, "Пожалуйста, введите ваше имя:");
        awaitingName.put(chatId, true);
    }

    public void handleTextMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);

        if (isAwaitingName(chatId)) {
            saveName(chatId, messageText);
            finishRegistration(chatId);
        } else {
            bot.sendMessage(chatId, "Извините, указанная команда не распознана");
        }
    }

    protected boolean isAwaitingName(long chatId) {
        return awaitingName.containsKey(chatId) && awaitingName.get(chatId);
    }

    private void saveName(long chatId, String name) {
        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(chatId);
        volunteer.setName(name);
        volunteer.setBusy(false);
        volunteerRepository.save(volunteer);
    }

    private void finishRegistration(long chatId) {
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);
        bot.sendMessage(chatId, "Регистрация завершена! Спасибо, что стали волонтером.");
        awaitingName.remove(chatId);
    }
}
