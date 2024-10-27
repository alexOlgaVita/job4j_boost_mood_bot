package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Класс будет обрабатывать меню бота.
 * @author Olga Ilyina
 * @version 1.0
 */
@Service
public class BotCommandHandler {
    private final UserRepository userRepository;
    private final MoodService moodService;
    private final TgUI tgUI;

    public BotCommandHandler(UserRepository userRepository,
                             MoodService moodService,
                             TgUI tgUI) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
    }

    /**
     * Обработка команд
     * @param message
     * @return
     */
    Optional<Content> commands(Message message) {
        switch (message.getText()) {
            case  ("/start"):
                return handleStartCommand(message.getChatId(), message.getContact().getUserId());
                break;
            case ("/week_mood_log"):
                return moodService.weekMoodLogCommand(message.getChatId(), message.getContact().getUserId());
                break;
            case ("/month_mood_log"):
                return moodService.monthMoodLogCommand(message.getChatId(), message.getContact().getUserId());
                break;
            case ("/award"):
                return moodService.awards(message.getChatId(), message.getContact().getUserId());
                break;
            default:
                return Optional.empty();
                break;
        }
    }

    Optional<Content> handleCallback(CallbackQuery callback) {
        var moodId = Long.valueOf(callback.getData());
        var user = userRepository.findByClientId(callback.getFrom().getId());
        return Stream.of(user).map(value -> moodService.chooseMood(value, moodId)).findFirst();
    }

    private Optional<Content> handleStartCommand(long chatId, Long clientId) {
        var user = new User();
        user.setClientId(clientId);
        user.setChatId(chatId);
        userRepository.save(user);
        var content = new Content(user.getChatId());
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }
}