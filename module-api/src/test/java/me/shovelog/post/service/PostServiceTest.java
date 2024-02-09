package me.shovelog.post.service;

import me.shovelog.ServiceTest;
import me.shovelog.category.domain.Category;
import me.shovelog.category.domain.CategoryItem;
import me.shovelog.category.repository.CategoryItemRepository;
import me.shovelog.category.repository.CategoryRepository;
import me.shovelog.exception.notfound.CategoryNotFoundException;
import me.shovelog.exception.notfound.PostNotFoundException;
import me.shovelog.post.domain.Post;
import me.shovelog.post.domain.PostStatus;
import me.shovelog.post.repository.PostRepository;
import me.shovelog.post.service.response.PostDetailResponse;
import me.shovelog.post.service.response.PostListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.*;

class PostServiceTest extends ServiceTest {
    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryItemRepository categoryItemRepository;

    @Test
    @DisplayName("제목으로 게시글을 조회할 수 있다")
    void findByTitle() {
        // given
        Category backend = createCategory("백엔드", "백엔드 설명");
        CategoryItem spring = createCategoryItem(backend, "Spring", "스프링 설명");
        Post di = createPost(spring, "Dependency Injection", "DI를 설명합니다",
                "DI는 의존성을 주입하여 실제 구현체를 감추는 기술로..[더보기]");

        // when
        PostDetailResponse response = postService.findByTitle("Dependency Injection");

        // then
        assertThat(response.id()).isEqualTo(di.getId());
        assertThat(response.title()).isEqualTo(di.getTitle());
    }

    @Test
    @DisplayName("존재하지 않는 게시글은 예외처리한다")
    void findByTitle2() {
        // when // then
        assertThatThrownBy(() -> postService.findByTitle("존재하지 않는 게시글 제목"))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("public으로 공개되지 않은 게시물은 예외처리한다")
    void findByTitle3() {
        // given
        Category backend = createCategory("백엔드", "백엔드 설명");
        CategoryItem spring = createCategoryItem(backend, "Spring", "스프링 설명");
        Post di = createPost(spring, "Dependency Injection", "DI를 설명합니다",
                "DI는 의존성을 주입하여 실제 구현체를 감추는 기술로..[더보기]", PostStatus.PRIVATE);

        // when // then
        assertThatThrownBy(() -> postService.findByTitle("Dependency Injection"))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("카테고리가 동일한 게시글 목록을 조회할 수 있다")
    void findByCategory() {
        // given
        Category backend = createCategory("백엔드", "백엔드 설명");
        CategoryItem spring = createCategoryItem(backend, "Spring", "스프링 설명");
        CategoryItem jvm = createCategoryItem(backend, "JVM", "JVM 설명");

        Post di = createPost(spring, "Dependency Injection", "DI를 설명합니다",
                "DI는 의존성을 주입하여 실제 구현체를 감추는 기술로..[더보기]");
        Post aop = createPost(spring, "AOP", "AOP를 설명합니다",
                "AOP는 프록시를 이용하여 공통 로직을 분리하는 기술로..[더보기]");
        Post gc = createPost(jvm, "GC", "GC를 설명합니다",
                "GC는 참조되지 않는 메모리를 해제하는...[더보기]");

        PageRequest request = PageRequest.of(0, 3);

        // when
        PostListResponse response = postService.findByCategory(backend.getName(), request);

        // then
        assertThat(response.categoryName()).isEqualTo(backend.getName());
        assertThat(response.categoryDescription()).isEqualTo(backend.getDescription());
        assertThat(response.posts()).extracting("postId", "title", "description")
                .containsExactly(
                        tuple(di.getId(), di.getTitle(), di.getDescription()),
                        tuple(aop.getId(), aop.getTitle(), aop.getDescription()),
                        tuple(gc.getId(), gc.getTitle(), gc.getDescription()));
    }

    @Test
    @DisplayName("존재하지 않는 카테고리는 예외처리한다")
    void findByCategory2() {
        // given
        PageRequest request = PageRequest.of(0, 3);

        // when // then
        assertThatThrownBy(() -> postService.findByCategory("존재하지 않는 카테고리 이름", request))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    @DisplayName("세부 카테고리가 동일한 게시글 목록을 조회할 수 있다")
    void findByCategoryItem() {
        // given
        Category backend = createCategory("백엔드", "백엔드 설명");
        CategoryItem spring = createCategoryItem(backend, "Spring", "스프링 설명");

        Post di = createPost(spring, "Dependency Injection", "DI를 설명합니다",
                "DI는 의존성을 주입하여 실제 구현체를 감추는 기술로..[더보기]");
        Post aop = createPost(spring, "AOP", "AOP를 설명합니다",
                "AOP는 프록시를 이용하여 공통 로직을 분리하는 기술로..[더보기]");

        PageRequest request = PageRequest.of(0, 2);

        // when
        PostListResponse response = postService.findByCategoryItem("Spring", request);

        // then
        assertThat(response.categoryName()).isEqualTo(spring.getItemName());
        assertThat(response.categoryDescription()).isEqualTo(spring.getDescription());
        assertThat(response.posts()).extracting("postId", "title", "description")
                .containsExactly(
                        tuple(di.getId(), di.getTitle(), di.getDescription()),
                        tuple(aop.getId(), aop.getTitle(), aop.getDescription()));
    }

    @Test
    @DisplayName("존재하지 않는 세부 카테고리는 예외처리한다")
    void findByCategoryItem2() {
        // given
        PageRequest request = PageRequest.of(0, 3);

        // when // then
        assertThatThrownBy(() -> postService.findByCategoryItem("존재하지 않는 세부 카테고리 이름", request))
                .isInstanceOf(CategoryNotFoundException.class);
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

    private Post createPost(CategoryItem categoryItem, String title, String description,
                            String content, PostStatus status) {
        return postRepository.save(Post.builder()
                .categoryItem(categoryItem)
                .title(title)
                .description(description)
                .content(content)
                .status(status)
                .build());
    }

    private Post createPost(CategoryItem categoryItem, String title, String description, String content) {
        return createPost(categoryItem, title, description, content, PostStatus.PUBLIC);
    }
}