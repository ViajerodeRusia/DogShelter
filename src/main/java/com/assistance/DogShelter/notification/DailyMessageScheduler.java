package com.assistance.DogShelter.notification;

import com.assistance.DogShelter.model.Pet;
import com.assistance.DogShelter.model.User;
import com.assistance.DogShelter.repositories.PetRepository;
import com.assistance.DogShelter.repositories.UserRepository;
import com.assistance.DogShelter.service.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component  //Аннотация указывает что это компонент Spring
public class DailyMessageScheduler {
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final TelegramBot telegramBot;
    private static final Logger logger = LoggerFactory.getLogger(DailyMessageScheduler.class);

    private static final int DAYS_TO_SEND = 30;    //срок отправления отчетов (дни)
    private static final int DAYS_EXTENSION = 45; //срок продления (дни)

    public DailyMessageScheduler(PetRepository petRepository, UserRepository userRepository, TelegramBot telegramBot) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 0 10 * * *") // Аннотация запускает метод каждый день в 10:00
    public void sendDailyMessage() {
        String message = "Добрый день! Ждем ваш ежедневный отчет.";
        String message2 = "Продленка";
        List<Pet> petList = petRepository.findAll();         //список всех питомцев
        for (Pet pet : petList) {                            //берем всех питомцев
            if (pet.getUser() != null) {                    //Проверяем по очереди есть ли у питомца user
                LocalDate date1 = pet.getDateAdoption();    //Если есть, то берем дату усыновления
                LocalDate today = LocalDate.now();          //Дату сегодня
                long differenceInDays = ChronoUnit.DAYS.between(date1, today); //Вычисляем разницу в днях
                User user = userRepository.findById(pet.getUser().getId());    //находим усыновителя этого питомца
                if (differenceInDays <= DAYS_TO_SEND && !user.isExtension()){  //Проверяем находится ли пользователь на продленке
                                                                               // и сколько уже дней присылает отчеты.
                    telegramBot.sendMessage(user.getChatId(), message);        //отправляем сообщение пользователю
                } else if (differenceInDays <= DAYS_EXTENSION && user.isExtension()) { //Проверяем находится ли пользователь на продленке
                                                                                       // и сколько уже дней присылает отчеты.
                    telegramBot.sendMessage(user.getChatId(), message2);        //отправляем сообщение пользователю
                }
            }
        }
    }
}
