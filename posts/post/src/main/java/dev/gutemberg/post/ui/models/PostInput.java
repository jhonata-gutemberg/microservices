package dev.gutemberg.post.ui.models;

import lombok.Data;

@Data
public class PostInput {
    private String title;
    private String author;
    private String message;
}
