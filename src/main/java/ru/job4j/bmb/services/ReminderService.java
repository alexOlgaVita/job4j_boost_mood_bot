package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Advice;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AdviceRepository;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static ru.job4j.bmb.services.MoodService.formatAdvice;

@Service
public class ReminderService {
    private final SentContent sentContent;
    private final MoodLogRepository moodLogRepository;
    private final TgUI tgUI;
    private final AdviceRepository adviceRepository;

    public ReminderService(SentContent sentContent,
                           MoodLogRepository moodLogRepository, TgUI tgUI, AdviceRepository adviceRepository) {
        this.sentContent = sentContent;
        this.moodLogRepository = moodLogRepository;
        this.tgUI = tgUI;
        this.adviceRepository = adviceRepository;
    }

    @Scheduled(fixedRateString = "${recommendation.alert.period}")
    public void remindUsers() {
        var startOfDay = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        var endOfDay = LocalDate.now()
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli() - 1;
        for (var user : moodLogRepository.findUsersWhoDidNotVoteToday(startOfDay, endOfDay)) {
            var content = new Content(user.getChatId());
            content.setText("Как настроение?");
            content.setMarkup(tgUI.buildButtons());
            sentContent.sent(content);
        }
    }

    @Scheduled(cron = "${dailyAdvice.interval.inCron}")
    public void dailyAdviceSent() {
        List<MoodLog> moodLogs = moodLogRepository.findAll().stream()
                .sorted(Comparator.comparing(MoodLog::getCreatedAt).reversed())
                .toList();
        Set<User> users = moodLogs.stream().map(MoodLog::getUser).collect(Collectors.toSet());
        for (var user : users) {
            boolean isGood = moodLogRepository.findAll().stream()
                    .filter(value -> value.getUser().equals(user))
                    .max(Comparator.comparing(MoodLog::getCreatedAt))
                    .get().getMood().isGood();
            List<Advice> advices = adviceRepository.findAll().stream()
                    .filter(value -> value.isGood() == isGood).toList();
            var content = new Content(user.getChatId());
            Random r = new Random();
            Advice advice = advices.get(r.nextInt(advices.size()));
            content.setText(formatAdvice(advice, "Совет дня"));
            sentContent.sent(content);
        }
    }
}