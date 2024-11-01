package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReminderService {
    private final SentContent sentContent;
    private final MoodLogRepository moodLogRepository;
    private final TgUI tgUI;
    private final ChooseDataService chooseDataService;

    public ReminderService(SentContent sentContent,
                           MoodLogRepository moodLogRepository, TgUI tgUI, ChooseDataService chooseDataService) {
        this.sentContent = sentContent;
        this.moodLogRepository = moodLogRepository;
        this.tgUI = tgUI;
        this.chooseDataService = chooseDataService;
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
            sentContent.sent(chooseDataService.getAdviceContent(user));
        }
    }
}