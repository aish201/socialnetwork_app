package com.SocialNetwork.dto;

import java.time.LocalDateTime;

public class PostResponseDTO {
    private Long id;
    private String content;
    private String imageUrl;
    private String username;
    private LocalDateTime createdAt;
    private int likes;

    public PostResponseDTO(Long id, String content, String imageUrl, String username, LocalDateTime createdAt, int likes) {
        this.id = id;
        this.content = content;
        this.imageUrl = imageUrl;
        this.username = username;
        this.createdAt = createdAt;
        this.likes = likes;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getContent() { return content; }
    public String getImageUrl() { return imageUrl; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getLikes() { return likes; }
}
