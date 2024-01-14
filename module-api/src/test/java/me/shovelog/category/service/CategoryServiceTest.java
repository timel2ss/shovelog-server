package me.shovelog.category.service;

import me.shovelog.ServiceTest;
import me.shovelog.category.domain.Category;
import me.shovelog.category.repository.CategoryRepository;
import me.shovelog.category.service.response.CategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class CategoryServiceTest extends ServiceTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 목록 조회")
    void findAllCategories() {
        // given
        Category category1 = createCategory("프론트엔드", "웹 프론트엔드 개발 일기");
        Category category2 = createCategory("백엔드", "백엔드 개발 일기");
        Category category3 = createCategory("데이터베이스", "데이터베이스 학습 일기");

        // when
        List<CategoryResponse> response = categoryService.findAll();

        // then
        assertThat(response).hasSize(3);
        assertThat(response).extracting("id", "name", "description")
                .containsExactlyInAnyOrder(
                        tuple(category1.getId(), category1.getName(), category1.getDescription()),
                        tuple(category2.getId(), category2.getName(), category2.getDescription()),
                        tuple(category3.getId(), category3.getName(), category3.getDescription())
                );
    }

    private Category createCategory(String name, String description) {
        return categoryRepository.save(
                Category.builder()
                        .name(name)
                        .description(description)
                        .build());
    }
}