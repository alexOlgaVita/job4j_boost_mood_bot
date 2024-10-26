package ru.job4j;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

/**
 * Класс, который следит за достижениями пользователя и награждает его за выполнение определенных действий.
 *
 * @author Olga Ilyina
 * @version 1.0
 */
@Service
public class AchievementService {
    @PostConstruct
    public void init() {
        System.out.println("achievementServiceBean is going through @PostConstruct init.");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("achievementServiceBean will be destroyed via @PreDestroy.");
    }
}
