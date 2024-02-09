package me.shovelog.post.service.response;

import lombok.Builder;
import me.shovelog.category.domain.Category;
import me.shovelog.category.domain.CategoryItem;
import me.shovelog.post.domain.Post;

import java.util.List;

@Builder
public record PostListResponse(
        String categoryName,
        String categoryDescription,
        List<PostResponse> posts
) {
    public static PostListResponse of(String categoryName, List<Post> posts) {
        return of(categoryName, "", toPostResponses(posts));
    }

    public static PostListResponse of(Category category, List<Post> posts) {
        return of(category.getName(), category.getDescription(), toPostResponses(posts));
    }

    public static PostListResponse of(CategoryItem categoryItem, List<Post> posts) {
        return of(categoryItem.getItemName(), categoryItem.getDescription(), toPostResponses(posts));
    }

    public static PostListResponse of(String categoryName, String categoryDescription, List<PostResponse> posts) {
        return PostListResponse.builder()
                .categoryName(categoryName)
                .categoryDescription(categoryDescription)
                .posts(posts)
                .build();
    }

    private static List<PostResponse> toPostResponses(List<Post> posts) {
        return posts.stream()
                .map(PostResponse::of)
                .toList();
    }
}
