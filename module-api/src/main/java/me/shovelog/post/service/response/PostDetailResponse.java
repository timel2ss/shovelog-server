package me.shovelog.post.service.response;

import lombok.Builder;
import me.shovelog.post.domain.Post;

import java.time.LocalDateTime;

@Builder
public record PostDetailResponse(
        Long id,
        String title,
        String description,
        String content,
        LocalDateTime createdAt
) {
    public static PostDetailResponse of(Post post) {
        return of(post.getId(), post.getTitle(), post.getDescription(), post.getContent(), post.getCreatedAt());
    }

    public static PostDetailResponse of(Long id, String title, String description,
                                        String content, LocalDateTime createdAt) {
        return PostDetailResponse.builder()
                .id(id)
                .title(title)
                .description(description)
                .content(content)
                .createdAt(createdAt)
                .build();
    }
}
