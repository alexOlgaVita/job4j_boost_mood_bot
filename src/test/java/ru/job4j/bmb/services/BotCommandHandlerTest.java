package ru.job4j.bmb.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.repositories.MoodFakeRepository;
import ru.job4j.bmb.repositories.MoodLogFakeRepository;
import ru.job4j.bmb.repositories.UserFakeRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {BotCommandHandler.class, UserFakeRepository.class,
        MoodFakeRepository.class, MoodLogFakeRepository.class})
class BotCommandHandlerTest {
    private static final long CHAT_ID = 100L;
    private static final long CLIENT_ID = 1L;
    private static final long MOOD_ID = 10L;
    private static final long USER_ID = 1L;

    @Autowired
    @Qualifier("userFakeRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("moodFakeRepository")
    private MoodFakeRepository moodRepository;

    @Autowired
    @Qualifier("moodLogFakeRepository")
    private MoodLogFakeRepository moodLogRepository;

    @MockBean
    private TgUI tgUI;

    @MockBean
    private MoodService moodService;

    @Autowired
    private BotCommandHandler botCommandHandler;

    @Test
    public void whenCommandStart() {
        Optional<Content> content;
        var contentExpected = new Content(CHAT_ID);
        contentExpected.setText("Как настроение?");
        userRepository.deleteAll();
        assertThat(userRepository.findByClientId(CLIENT_ID).stream().findFirst()).isEmpty();
        var inline = new InlineKeyboardButton();
        inline.setText("Good");
        inline.setCallbackData(String.valueOf(1));
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(List.of(inline));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        Mockito.when(tgUI.buildButtons())
                .thenReturn(inlineKeyboardMarkup);
        contentExpected.setMarkup(inlineKeyboardMarkup);
        content = botCommandHandler.commands(createTxtMessage("/start"));
        var userModel =  new ru.job4j.bmb.model.User(CLIENT_ID, CHAT_ID);
        userModel.setId(USER_ID);
        assertThat(userRepository.findByClientId(CLIENT_ID).stream()
                .findFirst().get()).isEqualTo(userModel);
        assertThat(content).isEqualTo(Optional.of(contentExpected));
    }

    @Test
    public void whenCommandWeekMoodLog() {
        Optional<Content> content;
        var contentExpected = new Content(CHAT_ID);
        contentExpected.setText("Отчет по настроению за последнюю неделю");
        Mockito.when(moodService.weekMoodLogCommand(CHAT_ID, CLIENT_ID))
                .thenReturn(Optional.of(contentExpected));
        content = botCommandHandler.commands(createTxtMessage("/week_mood_log"));
        assertThat(content).isEqualTo(Optional.of(contentExpected));
    }

    @Test
    public void whenCommandMonthMoodLog() {
        Optional<Content> content;
        var contentExpected = new Content(CHAT_ID);
        contentExpected.setText("Отчет по настроению за последний месяц");
        Mockito.when(moodService.monthMoodLogCommand(CHAT_ID, CLIENT_ID))
                .thenReturn(Optional.of(contentExpected));
        content = botCommandHandler.commands(createTxtMessage("/month_mood_log"));
        assertThat(content).isEqualTo(Optional.of(contentExpected));
    }

    @Test
    public void whenCommandAward() {
        Optional<Content> content;
        var contentExpected = new Content(CHAT_ID);
        contentExpected.setText("Ваши награды");
        Mockito.when(moodService.awards(CHAT_ID, CLIENT_ID))
                .thenReturn(Optional.of(contentExpected));
        content = botCommandHandler.commands(createTxtMessage("/award"));
        assertThat(content).isEqualTo(Optional.of(contentExpected));
    }

    @Test
    public void whenHandleCallback() {
        var contentExpected = new Content(CHAT_ID);
        contentExpected.setText("Рекомендуем:");
        var userModel =  new ru.job4j.bmb.model.User(CLIENT_ID, CHAT_ID);
        userModel.setId(USER_ID);
        userRepository.save(userModel);
        Mockito.when(moodService.chooseMood(userModel, MOOD_ID))
                .thenReturn(contentExpected);
        Optional<Content> content;
        CallbackQuery callbackquery = new CallbackQuery();
        var user = new User();
        user.setId(CLIENT_ID);
        callbackquery.setFrom(user);
        callbackquery.setData(String.valueOf(MOOD_ID));
        callbackquery.setId("CLIENT_ID");
        content = botCommandHandler.handleCallback(callbackquery);
        assertThat(content).isEqualTo(Optional.of(contentExpected));
    }

    private Message createTxtMessage(String text) {
        var message = new Message();
        var chat = new Chat();
        chat.setId(CHAT_ID);
        message.setChat(chat);
        var user = new User();
        user.setId(CLIENT_ID);
        message.setFrom(user);
        message.setText(text);
        return message;
    }
}