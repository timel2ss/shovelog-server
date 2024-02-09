package me.shovelog.post.service;

import lombok.RequiredArgsConstructor;
import me.shovelog.category.domain.Category;
import me.shovelog.category.domain.CategoryItem;
import me.shovelog.category.repository.CategoryItemRepository;
import me.shovelog.category.repository.CategoryRepository;
import me.shovelog.exception.notfound.CategoryNotFoundException;
import me.shovelog.exception.notfound.PostNotFoundException;
import me.shovelog.post.domain.Post;
import me.shovelog.post.repository.PostRepository;
import me.shovelog.post.service.response.PostDetailResponse;
import me.shovelog.post.service.response.PostListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryItemRepository categoryItemRepository;

    public PostDetailResponse findByTitle(String title) {
        Post post = postRepository.findByTitle(title)
                .orElseThrow(PostNotFoundException::new);
        if (!post.isPublic()) {
            throw new PostNotFoundException();
        }
        return PostDetailResponse.of(post);
    }

    public PostListResponse findAll(Pageable pageable) {
        List<Post> posts = postRepository.findAllPublic(pageable);
        return PostListResponse.of("전체 글 목록", posts);
    }

    public PostListResponse findByCategory(String categoryName, Pageable pageable) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(CategoryNotFoundException::new);
        List<Post> posts = postRepository.findByCategoryPublic(category.getId(), pageable);
        return PostListResponse.of(category, posts);
    }

    public PostListResponse findByCategoryItem(String categoryItemName, Pageable pageable) {
        CategoryItem categoryItem = categoryItemRepository.findByItemName(categoryItemName)
                .orElseThrow(CategoryNotFoundException::new);
        List<Post> posts = postRepository.findByCategoryItemPublic(categoryItem, pageable);
        return PostListResponse.of(categoryItem, posts);
    }
}
