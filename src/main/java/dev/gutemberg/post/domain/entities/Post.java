package dev.gutemberg.post.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Entity
public class Post {
    @Id
    private UUID id;
    private String title;
    private String body;
    private String author;
    private Long wordCount;
    private BigDecimal calculatedValue;

    public Post() {}

    public Post(final String title, final String body, final String author) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.body = body;
        this.author = author;
    }

    public UUID id() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String title() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String body() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String author() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Optional<Long> wordCount() {
        return Optional.ofNullable(wordCount);
    }

    public void setWordCount(Long wordCount) {
        this.wordCount = wordCount;
    }

    public Optional<BigDecimal> calculatedValue() {
        return Optional.ofNullable(calculatedValue);
    }

    public void setCalculatedValue(BigDecimal calculatedValue) {
        this.calculatedValue = calculatedValue;
    }

    public void setCalculatedValue(double calculatedValue) {
        this.calculatedValue = BigDecimal.valueOf(calculatedValue);
    }
}
