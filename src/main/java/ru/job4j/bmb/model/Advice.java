package ru.job4j.bmb.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mb_advice")
public class Advice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean good;

    private String suggestion;

    private String quote;

    public Advice() {
        super();
    }

    public Advice(boolean good, String suggestion, String quote) {
        this.good = good;
        this.suggestion = suggestion;
        this.quote = quote;
    }

    public Long getId() {
        return id;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public boolean isGood() {
        return good;
    }

    public String getQuote() {
        return quote;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}