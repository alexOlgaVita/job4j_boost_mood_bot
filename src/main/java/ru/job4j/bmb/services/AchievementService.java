package ru.job4j.bmb.services;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.event.UserEvent;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {
    private final SentContent sentContent;
    private final AchievementRepository achievementRepository;
    private final MoodLogRepository moodlogRepository;
    private final AwardRepository awardRepository;

    public AchievementService(SentContent sentContent,
                              AchievementRepository achievementRepository,
                              MoodLogRepository moodlogRepository, AwardRepository awardRepository) {
        this.sentContent = sentContent;
        this.achievementRepository = achievementRepository;
        this.moodlogRepository = moodlogRepository;
        this.awardRepository = awardRepository;
    }

    @Transactional
    @Override
    public void onApplicationEvent(UserEvent event) {
        var user = event.getUser();
        List<MoodLog> moodLogs = moodlogRepository.findAll().stream()
                .filter(value -> value.getUser().equals(user))
                .filter(value -> value.getMood().isGood())
                .toList();
        Optional<Award> award = awardRepository.findAll().stream()
                .filter(value -> value.getDays() == moodLogs.size())
                .findFirst();
        award.ifPresent(value -> achievementRepository.save(new Achievement(Instant.now().getEpochSecond(), user, value)));
        List<Achievement> achievements = achievementRepository.findAll().stream()
                .filter(value -> value.getUser().equals(user))
                .toList();
        var content = new Content(user.getChatId());
        if (!achievements.isEmpty()) {
            content.setText("Вы делаете успехи! На текущий момент у Вас имеются следующие награды:\n");
        } else {
            content.setText("На сегодня нет наград. Но не унывайте и продолжайте двигаться вперед!\n");
        }
        sentContent.sent(content);
        for (Achievement achievement : achievements) {
            content.setText(" :" + achievement.getAward().getDescription()
                    + ", :" + achievement.getAward().getDescription()
                    + ", :" + achievement.getAward().getDays()
                    + "\n");
            sentContent.sent(content);
        }
    }
}