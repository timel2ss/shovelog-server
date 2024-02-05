package me.shovelog.category.repository;

import me.shovelog.RepositoryTest;
import me.shovelog.category.domain.Category;
import me.shovelog.category.domain.CategoryItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

class CategoryRepositoryTest extends RepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryItemRepository categoryItemRepository;

    @Test
    @DisplayName("카테고리 조회 시 카테고리의 하위 아이템을 패치 조인으로 한꺼번에 가져와야 한다")
    void findAllFetchJoin() {
        // given
        Category frontend = createCategory("프론트엔드", "프론트엔드 설명");
        CategoryItem react = createCategoryItem(frontend, "React", "리액트 설명");
        CategoryItem vue = createCategoryItem(frontend, "Vue", "뷰 설명");

        Category backend = createCategory("백엔드", "백엔드 설명");
        CategoryItem spring = createCategoryItem(backend, "Spring", "스프링 설명");
        CategoryItem jvm = createCategoryItem(backend, "JVM", "JVM 설명");
        CategoryItem nestJs = createCategoryItem(backend, "NestJS", "NestJS 설명");

        Category algorithm = createCategory("알고리즘", "알고리즘 설명");
        CategoryItem boj = createCategoryItem(algorithm, "BOJ", "백준 설명");
        CategoryItem leetCode = createCategoryItem(backend, "LeetCode", "LeetCode 설명");

        // when
        List<Category> categories = categoryRepository.findAllFetchJoin();

        // then
        List<CategoryItem> categoryItems = categories.stream()
                .map(Category::getCategoryItems)
                .flatMap(List::stream)
                .toList();

        assertThat(categories).hasSize(3);
        assertThat(categories).extracting("name", "description")
                .contains(
                        tuple(frontend.getName(), frontend.getDescription()),
                        tuple(backend.getName(), backend.getDescription()),
                        tuple(algorithm.getName(), algorithm.getDescription()));
        assertThat(categoryItems).hasSize(7);
        assertThat(categoryItems).extracting("itemName", "description")
                .contains(
                        tuple(react.getItemName(), react.getDescription()),
                        tuple(vue.getItemName(), vue.getDescription()),
                        tuple(spring.getItemName(), spring.getDescription()),
                        tuple(jvm.getItemName(), jvm.getDescription()),
                        tuple(nestJs.getItemName(), nestJs.getDescription()),
                        tuple(boj.getItemName(), boj.getDescription()),
                        tuple(leetCode.getItemName(), leetCode.getDescription()));
    }

    private Category createCategory(String name, String description) {
        return save(Category.builder()
                .name(name)
                .description(description)
                .build());
    }

    private CategoryItem createCategoryItem(Category category, String itemName, String description) {
        CategoryItem item = save(CategoryItem.builder()
                .category(category)
                .itemName(itemName)
                .description(description)
                .build());
        category.addItem(item);
        return item;
    }
}