package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_mood_content")
public class MoodContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    private String text;

    public MoodContent(Mood mood, String text) {
        this.mood = mood;
        this.text = text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public Mood getMood() {
        return mood;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoodContent moodcontent = (MoodContent) o;
        return Objects.equals(id, moodcontent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
