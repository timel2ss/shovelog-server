package me.shovelog.category.service.response;

import lombok.Builder;
import me.shovelog.category.domain.Category;

import java.util.List;

@Builder
public record CategoryResponse(
        Long id,
        String name,
        String description,
        List<CategoryItemResponse> items
) {
    public static CategoryResponse of(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .items(CategoryItemResponse.of(category.getCategoryItems()))
                .build();
    }
}
