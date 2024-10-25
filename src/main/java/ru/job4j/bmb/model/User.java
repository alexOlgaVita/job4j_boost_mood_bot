package ru.job4j.bmb.model;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Profile("test")
@Repository
public class User {
    private Long id;
    private long clientId;
    private long chatId;

    public User(Long id, long clientId, long chatId) {
        this.id = id;
        this.clientId = clientId;
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public long getClientId() {
        return clientId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id
                && clientId == user.clientId
                && chatId == user.chatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, chatId);
    }
}
