package ru.job4j;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

/**
 * Класс основной, который использует Telegram API для получения и отправки сообщений
 * @author Olga Ilyina
 * @version 1.0
 */
@Service
public class TelegramBotService {
    @PostConstruct
    public void init() {
        System.out.println("telegramBotServiceBean is going through @PostConstruct init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("telegramBotServiceBean will be destroyed via @PreDestroy.");
    }
}