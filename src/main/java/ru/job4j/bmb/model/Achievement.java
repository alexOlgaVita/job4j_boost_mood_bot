package ru.job4j.bmb.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mb_achievement")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long createAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "award_id")
    private Award award;

    public Achievement() {
    }

    public Achievement(long createAt, User user, Award award) {
        this.createAt = createAt;
        this.user = user;
        this.award = award;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public Long getId() {
        return id;
    }

    public long getCreateAt() {
        return createAt;
    }

    public User getUser() {
        return user;
    }

    public Award getAward() {
        return award;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Achievement achievement = (Achievement) o;
        return Objects.equals(id, achievement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
