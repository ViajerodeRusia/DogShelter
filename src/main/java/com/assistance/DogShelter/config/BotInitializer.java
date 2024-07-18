package com.assistance.DogShelter.config;

import com.assistance.DogShelter.service.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Slf4j
public class BotInitializer {
    @Autowired
    TelegramBot telegramBot;
    @EventListener({ContextRefreshedEvent.class})
    public void  init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(telegramBot);
            log.info("Telegram bot registered successfully");
        } catch (TelegramApiException e) {
            log.error("Error occurred while registering Telegram bot:" + e.getMessage(), e);
        }

    }
}
