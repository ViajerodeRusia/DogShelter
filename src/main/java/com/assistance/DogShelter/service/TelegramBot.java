package com.assistance.DogShelter.service;

import com.assistance.DogShelter.config.BotConfig;
import com.assistance.DogShelter.controller.dto.PetDto;
import com.assistance.DogShelter.controller.dto.ShelterDto;
import com.assistance.DogShelter.db.entity.Shelter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;;
import java.util.*;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CallBackQueryHandler callBackQueryHandler;
    private final TextMessageHandler textMessageHandler;
    private final PetService petService;
    private final ShelterService shelterService;
    private final VolunteerService volunteerService;

    @Autowired
    public TelegramBot(BotConfig botConfig,
                       CallBackQueryHandler callBackQueryHandler,
                       TextMessageHandler textMessageHandler,
                       PetService petService,
                       ShelterService shelterService,
                       VolunteerService volunteerService) {
        this.botConfig = botConfig;
        this.callBackQueryHandler = callBackQueryHandler;
        this.textMessageHandler = textMessageHandler;
        this.petService = petService;
        this.shelterService = shelterService;
        this.volunteerService = volunteerService;

        // Инициализация списка команд для бота
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Начать"));
        listOfCommands.add(new BotCommand("/register_volunteer", "Регистрация волонтера"));
        listOfCommands.add(new BotCommand("/help", "Помощь"));

        try {
            // Установка команд для бота
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Ошибка при установке списка команд бота", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            callBackQueryHandler.handleCallbackQuery(update);
        } else {
            try {
                textMessageHandler.handleTextMessage(update);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
            log.info("Отправлено сообщение: \"" + textToSend + "\" в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: " + e.getMessage(), e);
        }
    }

    public void showShelterInfo(long chatId, long messageId, String text) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine4 = new ArrayList<>();

        InlineKeyboardButton buttonAddress = new InlineKeyboardButton("Наш адрес");
        buttonAddress.setCallbackData("ShelterAddress");

        InlineKeyboardButton buttonSchedule = new InlineKeyboardButton("Время работы");
        buttonSchedule.setCallbackData("Schedule");


        InlineKeyboardButton buttonDirections = new InlineKeyboardButton("Схема проезда");
        buttonDirections.setCallbackData("Directions");

        InlineKeyboardButton buttonRules = new InlineKeyboardButton("Правила ТБ");
        buttonRules.setCallbackData("Rules");

        InlineKeyboardButton buttonPassIssuance = new InlineKeyboardButton("Оформление пропуска");
        buttonPassIssuance.setCallbackData("PassIssuance");

        InlineKeyboardButton buttonCallVolunteer = new InlineKeyboardButton("Позвать волонтера");
        buttonCallVolunteer.setCallbackData("CallVolunteer");

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData("ComeBack1");

        rowInLine1.add(buttonAddress);
        rowInLine1.add(buttonSchedule);
        rowInLine2.add(buttonDirections);
        rowInLine2.add(buttonRules);
        rowInLine3.add(buttonPassIssuance);
        rowInLine3.add(buttonCallVolunteer);
        rowInLine4.add(buttonBack);

        rowsInline.add(rowInLine1);
        rowsInline.add(rowInLine2);
        rowsInline.add(rowInLine3);
        rowsInline.add(rowInLine4);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
            log.info("Отправлено меню информации о приюте в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке меню информации о приюте: " + e.getMessage(), e);
        }
    }

    public void showPets(long chatId, long shelterId) {
        // Получение списка питомцев из базы данных по shelterId
        List<PetDto> pets = (List<PetDto>) petService.getPetsByShelterId(shelterId);
        StringBuilder petsInfo = new StringBuilder("Наши питомцы:\n");

        // Проход по каждому питомцу и добавление информации о нем в строку сообщения
        for (PetDto pet : pets) {
            petsInfo.append("Имя: ").append(pet.getName()).append("\n")
                    .append("Порода: ").append(pet.getBreed()).append("\n")
                    .append("Возраст: ").append(pet.getAge()).append("\n\n");
        }

        // Отправка сообщения в Telegram
        sendMessage(chatId, petsInfo.toString());
    }

    public Long findRandomFreeVolunteer(){
        var freeVolunteers = volunteerService.findAllVolunteersIsBusy(false);
        if (freeVolunteers.isEmpty()) {
            return null;
        } else {
            Random random = new Random();
            var randomChatId = freeVolunteers.get(random.nextInt(freeVolunteers.size())).get().getChatId();
            return randomChatId;
        }

    }



    public void showDirection(long chatId) {
        // Логика получения адреса из базы данных по shelterId и отправка сообщений в Telegram
        Optional<ShelterDto> shelterDto = shelterService.findShelterById(1L);
        if (shelterDto.isPresent()) {
            sendMessage(chatId, "Адрес приюта:\n" + shelterDto.get().getAddress());
        } else {
            sendMessage(chatId, "К сожалению, не удалось найти приют с указанным идентификатором.");
        }
    }

    public void showGetPetMenu(long chatId, long messageId, String text) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine4 = new ArrayList<>();

        InlineKeyboardButton buttonOurPets = new InlineKeyboardButton("Наши питомцы");
        buttonOurPets.setCallbackData("OurPets");

        InlineKeyboardButton buttonDocumentsForAdopt = new InlineKeyboardButton("Документы для опекунства");
        buttonDocumentsForAdopt.setCallbackData("DocumentsForAdopt");

        InlineKeyboardButton buttonRulesIntroduction = new InlineKeyboardButton("Правила знакомства");
        buttonRulesIntroduction.setCallbackData("RulesIntroduction");

        InlineKeyboardButton buttonReasonsRefusal = new InlineKeyboardButton("Причины отказа");
        buttonReasonsRefusal.setCallbackData("ReasonsRefusal");

        InlineKeyboardButton buttonLeaveRequest = new InlineKeyboardButton("Оставить заявку");
        buttonLeaveRequest.setCallbackData("LeaveRequest");

        InlineKeyboardButton buttonCallVolunteer = new InlineKeyboardButton("Позвать волонтера");
        buttonCallVolunteer.setCallbackData("CallVolunteer");

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData("ComeBack1");

        rowInLine1.add(buttonOurPets);
        rowInLine1.add(buttonDocumentsForAdopt);
        rowInLine2.add(buttonRulesIntroduction);
        rowInLine2.add(buttonReasonsRefusal);
        rowInLine3.add(buttonLeaveRequest);
        rowInLine3.add(buttonCallVolunteer);
        rowInLine4.add(buttonBack);

        rowsInline.add(rowInLine1);
        rowsInline.add(rowInLine2);
        rowsInline.add(rowInLine3);
        rowsInline.add(rowInLine4);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
            log.info("Отправлено меню информации о приюте в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке меню информации о приюте: " + e.getMessage(), e);
        }
    }

    public void showRecommendationsMenu(long chatId, long messageId, String text) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine4 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine5 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine6 = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        InlineKeyboardButton buttonTransporting = new InlineKeyboardButton("Транспортировка питомца");
        buttonTransporting.setCallbackData("Transporting");

        InlineKeyboardButton buttonArrangeForPuppy = new InlineKeyboardButton("Оборудование места для щенка");
        buttonArrangeForPuppy.setCallbackData("Arrangeforpuppy");

        InlineKeyboardButton buttonArrangeForAdultAnimal = new InlineKeyboardButton("Оборудование места для взрослого питомца");
        buttonArrangeForAdultAnimal.setCallbackData("Arrangeforadultanimal");

        InlineKeyboardButton buttonArrangeForSpecialAnimal = new InlineKeyboardButton("Оборудование места для " +
                "питомца с ограниченными возможностями");
        buttonArrangeForSpecialAnimal.setCallbackData("Arrangeforspecialanimal");

        InlineKeyboardButton buttonAdviceDogHandler = new InlineKeyboardButton("Советы кинологов");
        buttonAdviceDogHandler.setCallbackData("Advicedoghandler");

        InlineKeyboardButton buttonDogHandler = new InlineKeyboardButton("Проверенные кинологи");
        buttonDogHandler.setCallbackData("Doghandler");

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData("ComeBack1");

        rowInLine1.add(buttonTransporting);
        rowInLine2.add(buttonArrangeForPuppy);
        rowInLine3.add(buttonArrangeForAdultAnimal);
        rowInLine4.add(buttonArrangeForSpecialAnimal);
        rowInLine5.add(buttonAdviceDogHandler);
        rowInLine5.add(buttonDogHandler);
        rowInLine6.add(buttonBack);

        rowsInLine.add(rowInLine1);
        rowsInLine.add(rowInLine2);
        rowsInLine.add(rowInLine3);
        rowsInLine.add(rowInLine4);
        rowsInLine.add(rowInLine5);
        rowsInLine.add(rowInLine6);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        try {
            execute(message);
            log.info("Отправлено меню Дополнительно в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке меню Дополнительно: " + e.getMessage(), e);
        }
    }

    public void showMoreMenu(long chatId, long messageId, String text) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        InlineKeyboardButton buttonRegisterVolunteer = new InlineKeyboardButton("Зарегистрироваться как волонтёр");
        buttonRegisterVolunteer.setCallbackData("RegisterVolunteer");

        InlineKeyboardButton buttonReport = new InlineKeyboardButton("Сдать отчёт");
        buttonReport.setCallbackData("Report");

        InlineKeyboardButton buttonCallVolunteer = new InlineKeyboardButton("Позвать волонтера");
        buttonCallVolunteer.setCallbackData("CallVolunteer");

        InlineKeyboardButton buttonBack = new InlineKeyboardButton("Назад");
        buttonBack.setCallbackData("ComeBack1");

        rowInLine1.add(buttonRegisterVolunteer);
        rowInLine2.add(buttonReport);
        rowInLine2.add(buttonCallVolunteer);
        rowInLine3.add(buttonBack);

        rowsInLine.add(rowInLine1);
        rowsInLine.add(rowInLine2);
        rowsInLine.add(rowInLine3);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        try {
            execute(message);
            log.info("Отправлено меню Дополнительно в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке меню Дополнительно: " + e.getMessage(), e);
        }
    }

    public void startCommandReceived(long chatId, String name) {
        // Формирование приветственного сообщения
        String greetingMessage = "Привет, " + name + "! Добро пожаловать в наш приют для собак!";
        sendMessage(chatId, greetingMessage);
        choosingMenu(chatId);
    }

    public void choosingMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите пункт меню:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton buttonMenu1 = new InlineKeyboardButton("Наш приют");
        buttonMenu1.setCallbackData("Shelter");

        InlineKeyboardButton buttonMenu2 = new InlineKeyboardButton("Взять питомца");
        buttonMenu2.setCallbackData("Get_pet");

        InlineKeyboardButton buttonMenu3 = new InlineKeyboardButton("Рекомендации");
        buttonMenu3.setCallbackData("Recommendations");

        InlineKeyboardButton buttonMenu4 = new InlineKeyboardButton("Дополнительно");
        buttonMenu4.setCallbackData("More");

        rowInline1.add(buttonMenu1);
        rowInline1.add(buttonMenu2);
        rowInline2.add(buttonMenu3);
        rowInline2.add(buttonMenu4);

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
            log.info("Отправлено сообщение с выбором приюта в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения с выбором приюта: " + e.getMessage(), e);
        }
    }
}