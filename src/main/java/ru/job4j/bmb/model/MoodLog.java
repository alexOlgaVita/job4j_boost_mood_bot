package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_mood_log")
public class MoodLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    private long createdAt;

    public MoodLog() {
    }

    public MoodLog(User user, Mood mood, long createdAt) {
        this.user = user;
        this.mood = mood;
        this.createdAt = createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Mood getMood() {
        return mood;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoodLog moodlog = (MoodLog) o;
        return Objects.equals(id, moodlog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
