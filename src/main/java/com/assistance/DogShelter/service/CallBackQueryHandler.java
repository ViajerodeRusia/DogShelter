package com.assistance.DogShelter.service;

import com.assistance.DogShelter.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class CallBackQueryHandler {
    private final VolunteerRegistrationService volunteerRegistrationService;
    private final TextMessageHandler textMessageHandler;
    private final ApplicationContext applicationContext;

    @Autowired
    public CallBackQueryHandler(VolunteerRegistrationService volunteerRegistrationService, TextMessageHandler textMessageHandler, ApplicationContext applicationContext) {
        this.volunteerRegistrationService = volunteerRegistrationService;
        this.textMessageHandler = textMessageHandler;
        this.applicationContext = applicationContext;
    }

    public void handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);

        try {
            switch (callbackData) {
                //Пункт меню "Наш приют"
                case "Shelter":
                    String text1 = "Наш приют:";
                    bot.showShelterInfo(chatId, messageId, text1);
                    break;
                //Пункт меню "Взять питомца"
                case "Get_pet":
                    String text2 = "Взять питомца:";

                    break;
                //Пункт меню "Рекомендации"
                case "Recommendations":
                    String text3 = "Рекомендации";
                    bot.showRecommendationsMenu(chatId, messageId, text3);
                    break;
                //Пункт меню "Дополнительно"
                case "More":
                    String text4 = "Дополнительно";
                    bot.showMoreMenu(chatId, messageId, text4);
                    break;
                case "ShelterAddress":
                    bot.sendMessage(chatId, "Наш адрес");
                    // загрузить из БД
                    break;
                case "Schedule":
                    bot.sendMessage(chatId, Constants.TEXTSHELTERSHEDULE);
                    break;
                case "Directions":
                    bot.sendMessage(chatId, "Схема проезда");
                    // загрузить из БД
                    break;
                case "Rules":
                    bot.sendMessage(chatId, Constants.TEXTSAFETYGUIDEFORDOGSSHELTER);
                    break;
                case "PassIssuance":
                    bot.sendMessage(chatId, Constants.TEXTPASSISSUANCE);
                    break;
                case "CallVolunteer":
                    bot.sendMessage(chatId, "Зовем волонтера");
                    break;
                case "RegisterVolunteer":
                    volunteerRegistrationService.registerVolunteer(update);
                    break;
                case "Transporting":
                    bot.sendMessage(chatId, Constants.RECOMMENDATIONSFORTRANSPORTINGPETS);
                    break;
                case "Arrangeforpuppy":
                    bot.sendMessage(chatId, Constants.ARRANGEFORPUPPY);
                    break;
                case "Arrangeforadultanimal":
                    bot.sendMessage(chatId, Constants.ARRANGEFORADULTANIMAL);
                    break;
                case "Arrangeforspecialanimal":
                    bot.sendMessage(chatId, Constants.ARRANGEFORSPECIALANIMAL);
                    break;
                case "Advicedoghandler":
                    bot.sendMessage(chatId, Constants.ADVICEDOGHADLER);
                    break;
                case "Doghandler":
                    bot.sendMessage(chatId, Constants.RECOMMENDEDDOGHANDLER);
                    break;
                case "ComeBack1":
                    bot.choosingMenu(chatId);
                    break;
                default:
                    log.warn("Unknown callback data received: " + callbackData);
            }
        } catch (Exception e) {
            log.error("Error while handling callback query: " + callbackData, e);
            bot.sendMessage(chatId, "Произошла ошибка при обработке вашего запроса. Пожалуйста, попробуйте позже.");
        }
    }
}
