package ru.job4j.bmb;

import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.services.BotCommandHandler;
import ru.job4j.bmb.services.TelegramBotService;

/**
 * Класс реализует внедрение зависимости Класса TelegramBotService от класса BotCommandHandler через конструктор.
 * @author Olga Ilyina
 * @version 1.0
 */
public class DIByDirectInjectMain {
    public static void main(String[] args) {
        var handler = new BotCommandHandler();
        var tg = new TelegramBotService(handler);
        tg.content(new Content(1L));
    }
}
