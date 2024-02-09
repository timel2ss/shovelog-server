package me.shovelog.post.repository;

import me.shovelog.RepositoryTest;
import me.shovelog.category.domain.Category;
import me.shovelog.category.domain.CategoryItem;
import me.shovelog.post.domain.Post;
import me.shovelog.post.domain.PostStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class PostRepositoryTest extends RepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("제목으로 게시글을 조회할 수 있다")
    void findByTitle() {
        // given
        Post di = createPost(null, "Dependency Injection", "DI를 설명합니다",
                "DI는 의존성을 주입하여 실제 구현체를 감추는 기술로..[더보기]");

        // when
        Post findPost = postRepository.findByTitle("Dependency Injection").get();

        // then
        assertThat(findPost.getId()).isEqualTo(di.getId());
        assertThat(findPost.getTitle()).isEqualTo(di.getTitle());
    }

    @Test
    @DisplayName("카테고리가 동일한 게시글 목록을 조회할 수 있다")
    void findByCategory() {
        // given
        Category backend = createCategory("백엔드", "백엔드 설명", 3L);
        CategoryItem spring = createCategoryItem(backend, "Spring", "스프링 설명", 2L);
        CategoryItem jvm = createCategoryItem(backend, "JVM", "JVM 설명", 1L);

        Post di = createPost(spring, "Dependency Injection", "DI를 설명합니다",
                "DI는 의존성을 주입하여 실제 구현체를 감추는 기술로..[더보기]");
        Post aop = createPost(spring, "AOP", "AOP를 설명합니다",
                "AOP는 프록시를 이용하여 공통 로직을 분리하는 기술로..[더보기]");
        Post gc = createPost(jvm, "GC", "GC를 설명합니다",
                "GC는 참조되지 않는 메모리를 해제하는...[더보기]");

        PageRequest request = PageRequest.of(0, 3);

        // when
        List<Post> response = postRepository.findByCategoryPublic(backend.getId(), request);

        // then
        assertThat(response).hasSize(3);
        assertThat(response).extracting("id", "title")
                .containsExactly(
                        tuple(di.getId(), di.getTitle()),
                        tuple(aop.getId(), aop.getTitle()),
                        tuple(gc.getId(), gc.getTitle()));
    }

    @Test
    @DisplayName("세부 카테고리가 동일한 게시글 목록을 조회할 수 있다")
    void findByCategoryItem() {
        // given
        Category backend = createCategory("백엔드", "백엔드 설명", 3L);
        CategoryItem spring = createCategoryItem(backend, "Spring", "스프링 설명", 2L);

        Post di = createPost(spring, "Dependency Injection", "DI를 설명합니다",
                "DI는 의존성을 주입하여 실제 구현체를 감추는 기술로..[더보기]");
        Post aop = createPost(spring, "AOP", "AOP를 설명합니다",
                "AOP는 프록시를 이용하여 공통 로직을 분리하는 기술로..[더보기]");

        PageRequest request = PageRequest.of(0, 2);

        // when
        List<Post> springResponse = postRepository.findByCategoryItemPublic(spring, request);

        // then
        assertThat(springResponse).hasSize(2);
        assertThat(springResponse).extracting("id", "title")
                .containsExactly(
                        tuple(di.getId(), di.getTitle()),
                        tuple(aop.getId(), aop.getTitle()));
    }

    private Category createCategory(String name, String description, Long categoryOrder) {
        return save(Category.builder()
                .name(name)
                .description(description)
                .categoryOrder(categoryOrder)
                .build());
    }

    private CategoryItem createCategoryItem(Category category, String itemName, String description, Long itemOrder) {
        CategoryItem item = save(CategoryItem.builder()
                .category(category)
                .itemName(itemName)
                .description(description)
                .itemOrder(itemOrder)
                .build());
        category.addItem(item);
        return item;
    }

    private Post createPost(CategoryItem categoryItem, String title, String description, String content) {
        return save(Post.builder()
                .categoryItem(categoryItem)
                .title(title)
                .description(description)
                .content(content)
                .status(PostStatus.PUBLIC)
                .build());
    }
}