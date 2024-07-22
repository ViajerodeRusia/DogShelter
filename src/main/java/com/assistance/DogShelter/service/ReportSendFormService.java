package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.entity.Photo;
import com.assistance.DogShelter.db.entity.Report;
import com.assistance.DogShelter.db.repository.PhotoRepository;
import com.assistance.DogShelter.db.repository.ReportRepository;
import com.assistance.DogShelter.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
public class ReportSendFormService {
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final PhotoRepository photoRepository;
    @Autowired
    private ApplicationContext applicationContext;

    private Map<Long, Boolean> awaitingText = new HashMap<>();
    private Map<Long, Boolean> awaitingPhoto = new HashMap<>();

    private Report report;  // Переменная для хранения текущего отчета
    private boolean isTextCreate = false;  // Флаг для проверки, создан ли текст отчета


    @Autowired
    public ReportSendFormService(UserRepository userRepository, ReportRepository reportRepository,
                                 PhotoRepository photoRepository) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.photoRepository = photoRepository;
    }

    public void sendReportForm(long chatId) {
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);
        if (userRepository.findByChatId(chatId).isPresent()) {
            bot.sendMessage(chatId, "Пожалуйста, напишите краткий отчёт о содержании питомца:");
            askForText(chatId);
        } else {
            bot.sendMessage(chatId, "Извините. Вы не являетесь зарегистрированным опекуном.");
        }
    }

    private void askForText(long chatId) {
        awaitingText.put(chatId, true);
    }

    private void askForPhoto(long chatId) {
        awaitingPhoto.put(chatId, true);
    }

    public void handleTextMessage(Update update) throws TelegramApiException, IOException {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (isAwaitingText(chatId) && !isTextCreate) {
                report = createReport(chatId, messageText);
                bot.sendMessage(chatId, "Пожалуйста, прикрепите фото:");
            } else if (isTextCreate && !isAwaitingPhoto(chatId)) {
                bot.sendMessage(chatId, "Необходимо прикрепить фото!");
            } else {
                bot.sendMessage(chatId, "Извините, указанная команда не распознана");
            }
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            if (isTextCreate) {
                askForPhoto(chatId);
                handlePhotoMessage(update);
                finishSendReport(chatId);
            } else if (!isTextCreate && isAwaitingText(chatId)) {
                bot.sendMessage(chatId, "Нужно прикрепить текст!");
            } else if (!isTextCreate && !isAwaitingPhoto(chatId)) {
                bot.sendMessage(chatId, "На данном этапе ничего прикреплять не нужно!");
            }
        }
    }
    public void handlePhotoMessage(Update update) throws TelegramApiException, IOException {
        // Проверяем, есть ли сообщение и содержит ли оно фото
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            // Получаем список фотографий из сообщения
            List<PhotoSize> photos = update.getMessage().getPhoto();

            // Находим фотографию с наибольшим размером файла
            PhotoSize largestPhoto = photos.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElseThrow();  // Если фотографий нет, выбрасываем исключение

            // Получаем fileId наибольшей фотографии
            String fileId = largestPhoto.getFileId();

            // Получаем URL файла по его fileId
            String fileUrl = getFileUrl(fileId);

            // Загружаем фото по URL и сохраняем его в виде массива байтов
            byte[] imageData = downloadPhoto(fileUrl);

            // Определяем путь для сохранения файла, используя ID отчета и расширение файла
            Path filePath = Path.of("/photoDir", report.getId() + "." + getExtension(fileUrl));

            // Создаем необходимые директории, если они не существуют
            Files.createDirectories(filePath.getParent());

            // Удаляем файл, если он уже существует
            Files.deleteIfExists(filePath);

            // Сохраняем фото на диск
            try (OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                 BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
                bos.write(imageData);  // Записываем данные фото в файл
            }

            // Создаем объект Photo и заполняем его данными
            Photo photo = new Photo();
            photo.setFileId(fileId);
            photo.setFilePath(filePath.toString());
            photo.setData(imageData);
            photo.setReport(report);  // Устанавливаем отчет для фото

            // Сохраняем информацию о фото в базе данных
            photoRepository.save(photo);

            // Логируем успешное сохранение фото на диск и в базу данных
            System.out.println("Фото успешно сохранено на диск и в базу данных: " + filePath.toString());
        }
    }
    public byte[] downloadPhoto(String fileUrl) throws IOException {
        // Используем try-with-resources для автоматического закрытия ресурсов
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(fileUrl));
             InputStream inputStream = response.getEntity().getContent();
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            // Создаем буфер для чтения данных
            byte[] data = new byte[1024];
            int bytesRead;

            // Читаем данные из inputStream в буфер, пока не достигнем конца потока
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                // Записываем прочитанные данные в ByteArrayOutputStream
                buffer.write(data, 0, bytesRead);
            }

            // Проверяем размер загруженного файла, чтобы убедиться, что он не пустой
            if (buffer.size() == 0) {
                throw new IOException("Загружен пустой файл.");
            }

            // Возвращаем загруженные данные в виде массива байтов
            return buffer.toByteArray();
        }
    }

    public String getFileUrl(String fileId) throws TelegramApiException {
        // Получаем экземпляр TelegramBot из контекста приложения
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);

        // Создаем объект GetFile и устанавливаем идентификатор файла
        GetFile getFile = new GetFile();
        getFile.setFileId(fileId);

        // Выполняем запрос к Telegram API для получения объекта файла
        org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFile);

        // Получаем URL файла, используя токен бота
        return file.getFileUrl(bot.getBotToken());
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    protected boolean isAwaitingText(long chatId) {
        return awaitingText.containsKey(chatId) && awaitingText.get(chatId);
    }

    protected boolean isAwaitingPhoto(long chatId) {
        return awaitingPhoto.containsKey(chatId) && awaitingPhoto.get(chatId);
    }


    private Report createReport(long chatId, String text) {
        Report report = new Report();
        report.setText(text);
        report.setDate(LocalDateTime.now().toLocalDate());
        report.setCheckReport(false);
        report.setUser(userRepository.findByChatId(chatId).get());
        Report savedReport = reportRepository.save(report);  // Сохраняем отчет в базе данных
        isTextCreate = true;
        return savedReport;
    }

    private void finishSendReport(long chatId) {
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);
        bot.sendMessage(chatId, "Отчёт отправлен! Спасибо.");
        isTextCreate = false;
        awaitingText.remove(chatId);
        awaitingPhoto.remove(chatId);
    }
}