package me.shovelog.category.service.response;

import lombok.Builder;
import me.shovelog.category.domain.CategoryItem;

import java.util.List;

@Builder
public record CategoryItemResponse(
        Long id,
        String itemName,
        String description
) {
    public static CategoryItemResponse of(CategoryItem categoryItem) {
        return CategoryItemResponse.builder()
                .id(categoryItem.getId())
                .itemName(categoryItem.getItemName())
                .description(categoryItem.getDescription())
                .build();
    }

    public static List<CategoryItemResponse> of(List<CategoryItem> categoryItems) {
        return categoryItems.stream()
                .map(CategoryItemResponse::of)
                .toList();
    }
}
