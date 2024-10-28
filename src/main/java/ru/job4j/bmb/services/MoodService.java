package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.MoodRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MoodService {
    private final MoodLogRepository moodLogRepository;
    private final MoodRepository moodrepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       MoodRepository moodrepository) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.moodrepository = moodrepository;
    }

    public Content chooseMood(User user, Long moodId) {
        Mood mood = moodrepository.findAll().stream()
                .filter(value -> Objects.equals(value.getId(), moodId))
                .findFirst().orElse(null);
        moodLogRepository.save(new MoodLog(user, mood, Instant.now().getEpochSecond()));
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        content.setText(formatMoodLogs(periodMoodLogCommand(chatId, clientId, 7 * 24 * 60 * 60), "Отчет по настроению за последнюю неделю"));
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        content.setText(formatMoodLogs(periodMoodLogCommand(chatId, clientId, 30 * 7 * 24 * 60 * 60), "Отчет по настроению за последний месяц"));
        return Optional.of(content);
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\nNo mood logs found.";
        }
        var sb = new StringBuilder(title + ":\n");
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            sb.append(formattedDate).append(": ").append(log.getMood().getText()).append("\n");
        });
        return sb.toString();
    }

    private List<MoodLog> periodMoodLogCommand(long chatId, Long clientId, long seconds) {
        User user = userRepository.findAll().stream()
                .filter(value -> Objects.equals(value.getClientId(), clientId) && Objects.equals(value.getChatId(), chatId))
                .findFirst().orElse(null);
        return moodLogRepository.findAll().stream()
                .filter(value -> value.getUser().equals(user) && (Instant.now().getEpochSecond() - value.getCreatedAt() <= seconds))
                .toList();
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        var content = new Content(chatId);
        return Optional.of(content);
    }
}
