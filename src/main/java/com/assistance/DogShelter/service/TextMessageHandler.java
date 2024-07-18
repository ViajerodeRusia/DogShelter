package com.assistance.DogShelter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TextMessageHandler {

    @Autowired
    private VolunteerRegistrationService volunteerRegistrationService;

    @Autowired
    private ApplicationContext applicationContext;

    public void handleTextMessage(Update update) {
        // Проверка, что сообщение является текстовым
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            TelegramBot bot = applicationContext.getBean(TelegramBot.class);

            switch (messageText) {
                case "/start":
                    bot.startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    bot.sendMessage(chatId, "Список доступных команд:\n/start - Начать работу с ботом (меню)\n/register_volunteer - Регистрация волонтера\n/help - Список команд");
                    break;
                case "/register_volunteer":
                    volunteerRegistrationService.registerVolunteer(update);
                    break;
                default:
                    // Обработка других текстовых сообщений
                    volunteerRegistrationService.handleTextMessage(update);
                    break;
            }
        } else {
            // Если сообщение не текстовое, можно добавить другую обработку
            log.info("Получено не текстовое сообщение: " + update);
        }
    }
}
