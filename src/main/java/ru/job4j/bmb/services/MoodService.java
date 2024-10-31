package ru.job4j.bmb.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.event.UserEvent;
import ru.job4j.bmb.model.*;
import ru.job4j.bmb.repository.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MoodService {
    private final ApplicationEventPublisher publisher;
    private final MoodLogRepository moodLogRepository;
    private final MoodRepository moodrepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final AwardRepository awardRepository;
    private final AdviceRepository adviceRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       MoodRepository moodrepository,
                       AwardRepository awardRepository,
                       ApplicationEventPublisher publisher,
                       AdviceRepository adviceRepository) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.moodrepository = moodrepository;
        this.awardRepository = awardRepository;
        this.publisher = publisher;
        this.adviceRepository = adviceRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        Mood mood = moodrepository.findAll().stream()
                .filter(value -> Objects.equals(value.getId(), moodId))
                .findFirst().orElse(null);
        moodLogRepository.save(new MoodLog(user, mood, Instant.now().getEpochSecond()));
        publisher.publishEvent(new UserEvent(this, user));
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        content.setText(formatMoodLogs(periodMoodLogCommand(chatId, clientId, 7 * 24 * 60 * 60), "Отчет по настроению за последнюю неделю"));
        return Optional.of(content);
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        var content = new Content(chatId);
        content.setText(formatMoodLogs(periodMoodLogCommand(chatId, clientId, 30 * 24 * 60 * 60), "Отчет по настроению за последний месяц"));
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
        User user = userRepository.findAll().stream()
                .filter(value -> Objects.equals(value.getClientId(), clientId) && Objects.equals(value.getChatId(), chatId))
                .findFirst().orElse(null);
        List<Award> achievementAwards = achievementRepository.findAll().stream()
                .filter(value -> value.getUser().equals(user)
                        && (value.getCreateAt() <= Instant.now().getEpochSecond()
                        && (value.getCreateAt() >= Instant.now().getEpochSecond() - 30 * 24 * 60 * 60)))
                .map(Achievement::getAward)
                .toList();
        List<Award> awards = awardRepository.findAll().stream()
                .filter(achievementAwards::contains).toList();
        content.setText(formatAwards(awards, "Ваши достижения за месяц"));
        return Optional.of(content);
    }

    public Optional<Content> dailyAdvice(long chatId, Long clientId) {
        Random r = new Random();
        var content = new Content(chatId);
        User user = userRepository.findAll().stream()
                .filter(value -> Objects.equals(value.getClientId(), clientId) && Objects.equals(value.getChatId(), chatId))
                .findFirst().orElse(null);
        boolean isGood = moodLogRepository.findAll().stream()
                .filter(value -> value.getUser().equals(user))
                .max(Comparator.comparing(MoodLog::getCreatedAt))
                .get().getMood().isGood();
        List<Advice> advices = adviceRepository.findAll().stream()
                .filter(value -> value.isGood() == isGood).toList();
        Advice advice = advices.get(r.nextInt(advices.size()));
        content.setText(formatAdvice(advice, "Совет дня"));
        return Optional.of(content);
    }

    private String formatAwards(List<Award> awards, String title) {
        if (awards.isEmpty()) {
            return title + ":\nNo awards found.";
        }
        var sb = new StringBuilder(title + ":\n");
        awards.forEach(award -> {
            sb.append("-- Количество дней: ").append(award.getDays()).append(" --").append("\n")
                    .append("Заголовок: ").append(award.getTitle()).append("\n")
                    .append("Описание награды: ").append(award.getDescription()).append("\n");
        });
        return sb.toString();
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