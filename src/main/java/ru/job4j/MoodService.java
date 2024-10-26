package ru.job4j;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

/**
 * Класс, отвечающий за обработку запросов пользователя в зависимости от его настроения.
 * @author Olga Ilyina
 * @version 1.0
 */
@Service
public class MoodService {
    @PostConstruct
    public void init() {
        System.out.println("moodServiceBean is going through @PostConstruct init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("moodServiceBean will be destroyed via @PreDestroy.");
    }
}
