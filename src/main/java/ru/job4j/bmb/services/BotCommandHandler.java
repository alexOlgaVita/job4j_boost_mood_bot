package ru.job4j.bmb.services;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;

/**
 * Класс будет обрабатывать меню бота.
 * @author Olga Ilyina
 * @version 1.0
 */
@Service
public class BotCommandHandler implements BeanNameAware {
    void receive(Content content) {
        System.out.println(content);
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println(beanName);
    }
}
