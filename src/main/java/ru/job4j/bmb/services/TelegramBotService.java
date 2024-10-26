package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;

/**
 * Класс описывает интеграцию с Telegram API. Является входной точкой в приложение.
 * @author Olga Ilyina
 * @version 1.0
 */
@Service
public class TelegramBotService {
    private final BotCommandHandler handler;

    public TelegramBotService(BotCommandHandler handler) {
        this.handler = handler;
    }

    public void content(Content content) {
        handler.receive(content);
    }
}
