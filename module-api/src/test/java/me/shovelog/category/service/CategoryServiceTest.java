package me.shovelog.category.service;

import me.shovelog.ServiceTest;
import me.shovelog.category.domain.Category;
import me.shovelog.category.domain.CategoryItem;
import me.shovelog.category.repository.CategoryItemRepository;
import me.shovelog.category.repository.CategoryRepository;
import me.shovelog.category.service.response.CategoryItemResponse;
import me.shovelog.category.service.response.CategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class CategoryServiceTest extends ServiceTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryItemRepository categoryItemRepository;

    @Test
    @DisplayName("카테고리 목록 조회")
    void findAllCategories() {
        // given
        Category category1 = createCategory("프론트엔드", "웹 프론트엔드 개발 일기");
        CategoryItem react = createCategoryItem(category1, "React", "리액트 설명");
        CategoryItem vue = createCategoryItem(category1, "Vue", "뷰 설명");

        Category category2 = createCategory("백엔드", "백엔드 개발 일기");
        CategoryItem spring = createCategoryItem(category2, "Spring", "스프링 설명");
        CategoryItem jvm = createCategoryItem(category2, "JVM", "JVM 설명");
        CategoryItem nestJs = createCategoryItem(category2, "NestJS", "NestJS 설명");

        Category category3 = createCategory("데이터베이스", "데이터베이스 학습 일기");
        CategoryItem dbms = createCategoryItem(category3, "DBMS", "DBMS 설명");
        CategoryItem sql = createCategoryItem(category3, "SQL", "SQL 설명");

        // when
        List<CategoryResponse> categoryResponse = categoryService.findAll();

        // then
        List<CategoryItemResponse> categoryItemResponse = categoryResponse.stream()
                .map(CategoryResponse::items)
                .flatMap(Collection::stream)
                .toList();

        assertThat(categoryResponse).hasSize(3);
        assertThat(categoryResponse).extracting("id", "name", "description")
                .containsExactlyInAnyOrder(
                        tuple(category1.getId(), category1.getName(), category1.getDescription()),
                        tuple(category2.getId(), category2.getName(), category2.getDescription()),
                        tuple(category3.getId(), category3.getName(), category3.getDescription())
                );
        assertThat(categoryItemResponse).hasSize(7);
        assertThat(categoryItemResponse).extracting("id", "itemName", "description")
                .containsExactlyInAnyOrder(
                        tuple(react.getId(), react.getItemName(), react.getDescription()),
                        tuple(vue.getId(), vue.getItemName(), vue.getDescription()),
                        tuple(spring.getId(), spring.getItemName(), spring.getDescription()),
                        tuple(jvm.getId(), jvm.getItemName(), jvm.getDescription()),
                        tuple(nestJs.getId(), nestJs.getItemName(), nestJs.getDescription()),
                        tuple(dbms.getId(), dbms.getItemName(), dbms.getDescription()),
                        tuple(sql.getId(), sql.getItemName(), sql.getDescription())
                );
    }

    private Category createCategory(String name, String description) {
        return categoryRepository.save(
                Category.builder()
                        .name(name)
                        .description(description)
                        .build());
    }

    private CategoryItem createCategoryItem(Category category, String itemName, String description) {
        CategoryItem item = categoryItemRepository.save(CategoryItem.builder()
                .category(category)
                .itemName(itemName)
                .description(description)
                .build());
        category.addItem(item);
        return item;
    }
}