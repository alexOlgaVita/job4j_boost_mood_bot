package ru.job4j;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

/**
 * Класс для обработки команд, поступающих от пользователей (например, выбор настроения, запрос рекомендаций).
 * @author Olga Ilyina
 * @version 1.0
 */
@Service
public class BotCommandHandler {
    @PostConstruct
    public void init() {
        System.out.println("botCommandHandlerBean is going through @PostConstruct init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("botCommandHandlerBean will be destroyed via @PreDestroy.");
    }
}