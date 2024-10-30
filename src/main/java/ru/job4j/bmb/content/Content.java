package ru.job4j.bmb.content;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Objects;

/**
 * Класс описывает Базовую модель данных "Контент".
 * Этот объект будет описывать сообщения системы, которые могут содержать текст, аудио, видео.
 *
 * @author Olga Ilyina
 * @version 1.0
 */
public class Content {
    private final Long chatId;
    private String text;
    private InputFile photo;
    private InlineKeyboardMarkup markup;
    private InputFile audio;
    private InputFile video;

    public Content(Long chatId) {
        this.chatId = chatId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPhoto(InputFile photo) {
        this.photo = photo;
    }

    public void setMarkup(InlineKeyboardMarkup markup) {
        this.markup = markup;
    }

    public void setAudio(InputFile audio) {
        this.audio = audio;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getText() {
        return text;
    }

    public InputFile getPhoto() {
        return photo;
    }

    public InlineKeyboardMarkup getMarkup() {
        return markup;
    }

    public InputFile getAudio() {
        return audio;
    }

    public void setVideo(InputFile video) {
        this.video = video;
    }

    public InputFile getVideo() {
        return video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Content content = (Content) o;
        return (Objects.equals(chatId, content.chatId)
                && Objects.equals(text, content.text)
                && Objects.equals(markup, content.markup)
                && Objects.equals(audio, content.audio)
                && Objects.equals(photo, content.photo)
                && Objects.equals(video, content.video)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }
}