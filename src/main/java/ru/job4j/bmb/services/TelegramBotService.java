package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.exception.SentContentException;
import ru.job4j.bmb.repository.UserRepository;

/**
 * Класс описывает интеграцию с Telegram API. Является входной точкой в приложение.
 *
 * @author Olga Ilyina
 * @version 1.0
 */
@Service
public class TelegramBotService extends TelegramLongPollingBot implements SentContent {
    private final BotCommandHandler handler;
    private final String botName;
    private final String botToken;
    private final UserRepository userRepository;
    private final TgUI tgUI;

    public TelegramBotService(@Value("${telegram.bot.name}") String botName,
                              @Value("${telegram.bot.token}") String botToken,
                              BotCommandHandler handler,
                              UserRepository userRepository,
                              TgUI tgUI) {
        super(botToken);
        this.handler = handler;
        this.botName = botName;
        this.botToken = botToken;
        this.userRepository = userRepository;
        this.tgUI = tgUI;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handler.handleCallback(update.getCallbackQuery())
                    .ifPresent(this::sent);
        } else if (update.hasMessage() && update.getMessage().getText() != null) {
            handler.commands(update.getMessage())
                    .ifPresent(this::sent);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void sent(Content content) {
        try {
            if (content.getVideo() != null) {
                var message = new SendAudio();
                message.setChatId(String.valueOf(content.getChatId()));
                message.setAudio(content.getVideo());
                execute(message);
            }
            if (content.getText() != null && content.getMarkup() != null) {
                var message = new SendMessage();
                message.setChatId(String.valueOf(content.getChatId()));
                message.setText(content.getText());
                message.setReplyMarkup(content.getMarkup());
                execute(message);
            }
            if (content.getText() != null && content.getMarkup() == null) {
                var message = new SendMessage();
                message.setChatId(String.valueOf(content.getChatId()));
                message.setText(content.getText());
                execute(message);
            }
            if (content.getPhoto() != null) {
                var message = new SendPhoto();
                message.setChatId(String.valueOf(content.getChatId()));
                message.setPhoto(content.getVideo());
                execute(message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
            throw new SentContentException(e.getMessage(), e);
        }
    }
}