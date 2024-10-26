package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;

/**
 * Класс будет обрабатывать меню бота.
 * @author Olga Ilyina
 * @version 1.0
 */
@Service
public class BotCommandHandler {
    void receive(Content content) {
        System.out.println(content);
    }
}
