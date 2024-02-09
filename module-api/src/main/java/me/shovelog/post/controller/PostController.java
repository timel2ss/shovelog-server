package me.shovelog.post.controller;

import lombok.RequiredArgsConstructor;
import me.shovelog.post.service.PostService;
import me.shovelog.post.service.response.PostDetailResponse;
import me.shovelog.post.service.response.PostListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/api/v1/posts")
    public ResponseEntity<PostListResponse> getPosts(
            @PageableDefault(sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(postService.findAll(pageable));
    }

    @GetMapping("/api/v1/posts/{title}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable("title") String title) {
        return ResponseEntity.ok(postService.findByTitle(title));
    }

    @GetMapping("/api/v1/category/{categoryName}/posts")
    public ResponseEntity<PostListResponse> getPostsByCategory(
            @PathVariable("categoryName") String categoryName,
            @PageableDefault(sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(postService.findByCategory(categoryName, pageable));
    }

    @GetMapping("/api/v1/categoryItem/{itemName}/posts")
    public ResponseEntity<PostListResponse> getPostsByCategoryItem(
            @PathVariable("itemName") String itemName,
            @PageableDefault(sort = "createdAt") Pageable pageable
    ) {
        return ResponseEntity.ok(postService.findByCategoryItem(itemName, pageable));
    }
}
