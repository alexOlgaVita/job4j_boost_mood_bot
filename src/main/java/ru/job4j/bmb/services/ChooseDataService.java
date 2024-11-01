package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Advice;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AdviceRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class ChooseDataService {
    private final UserRepository userRepository;
    private final AdviceRepository adviceRepository;
    private final MoodLogRepository moodLogRepository;

    public ChooseDataService(UserRepository userRepository, AdviceRepository adviceRepository, MoodLogRepository moodLogRepository) {
        this.userRepository = userRepository;
        this.adviceRepository = adviceRepository;
        this.moodLogRepository = moodLogRepository;
    }

    public User getUserByChatClientId(long chatId, long clientId) {
        return userRepository.findAll().stream()
                .filter(value -> Objects.equals(value.getClientId(), clientId) && Objects.equals(value.getChatId(), chatId))
                .findFirst().orElse(null);
    }

    public Content getAdviceContent(User user) {
        boolean isGood = moodLogRepository.findAll().stream()
                .filter(value -> value.getUser().equals(user))
                .max(Comparator.comparing(MoodLog::getCreatedAt))
                .get().getMood().isGood();
        List<Advice> advices = adviceRepository.findAll().stream()
                .filter(value -> value.isGood() == isGood).toList();
        Random r = new Random();
        var content = new Content(userRepository.findAll().stream()
                .filter(value -> value.equals(user))
                .findFirst()
                .map(value -> value.getClientId())
                .get());
        Advice advice = advices.get(r.nextInt(advices.size()));
        content.setText(formatAdvice(advice, "Совет дня"));
        return content;
    }

    public static String formatAdvice(Advice advice, String title) {
        if (advice == null) {
            return title + ":\nДля начала советуем ответить, какое у Вас сегодня настроение";
        }
        var sb = new StringBuilder(title + ":\n");
        sb.append("Цитата: ").append(advice.getQuote()).append("\nРекомендуем: ").append(advice.getSuggestion()).append("\n");
        return sb.toString();
    }
}