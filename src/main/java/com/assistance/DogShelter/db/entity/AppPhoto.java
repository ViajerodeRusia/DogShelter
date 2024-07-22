package com.assistance.DogShelter.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_photo")
@Entity
public class AppPhoto {
    /* AppPhoto — это сущность, представляющая фотографию, полученную через Telegram. */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String telegramFileId;
    /*
    Это поле хранит ID файла, полученный из Telegram.
    telegramFileId используется для идентификации и взаимодействия с файлом в системе Telegram.
    */

    @OneToOne
    private BinaryContent binaryContent;
    /*
    Поле binaryContent представляет собой ссылку на объект BinaryContent, который хранит бинарное
    содержимое файла (сами данные).
    */

    private Integer fileSize;
/*
    Поле fileSize хранит размер файла в байтах.
    fileSize используется для хранения информации о размере загруженной фотографии.
*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppPhoto appPhoto = (AppPhoto) o;
        return telegramFileId != null && Objects.equals(telegramFileId, appPhoto.telegramFileId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
