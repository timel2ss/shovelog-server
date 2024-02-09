package me.shovelog.post.service.response;

import lombok.Builder;
import me.shovelog.post.domain.Post;

import java.time.LocalDateTime;

@Builder
public record PostResponse(
        Long postId,
        String title,
        String description,
        LocalDateTime createdAt
) {
    public static PostResponse of(Post post) {
        return of(post.getId(), post.getTitle(), post.getDescription(), post.getCreatedAt());
    }

    public static PostResponse of(Long postId, String title, String description, LocalDateTime createdAt) {
        return PostResponse.builder()
                .postId(postId)
                .title(title)
                .description(description)
                .createdAt(createdAt)
                .build();
    }
}
