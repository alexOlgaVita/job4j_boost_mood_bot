package ru.job4j;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

/**
 * Класс, который управляет ежедневными напоминаниями и уведомлениями.
 * @author Olga Ilyina
 * @version 1.0
 */
@Service
public class ReminderService {
    @PostConstruct
    public void init() {
        System.out.println("reminderServiceBean is going through @PostConstruct init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("reminderServiceBean will be destroyed via @PreDestroy.");
    }
}
