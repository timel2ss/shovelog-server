package me.shovelog.post.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.shovelog.category.domain.CategoryItem;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryItem categoryItem;

    @Column(unique = true, nullable = false)
    private String title;

    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    private Post(CategoryItem categoryItem, String title, String description, String content, PostStatus status) {
        this.categoryItem = categoryItem;
        this.title = title;
        this.description = description;
        this.content = content;
        this.status = status;
    }

    public boolean isPublic() {
        return status == PostStatus.PUBLIC;
    }
}
