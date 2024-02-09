package me.shovelog.post.controller;

import me.shovelog.RestDocs;
import me.shovelog.post.service.response.PostDetailResponse;
import me.shovelog.post.service.response.PostListResponse;
import me.shovelog.post.service.response.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends RestDocs {

    @Test
    @DisplayName("전체 게시글 목록 조회 API")
    void getPosts() throws Exception {
        // given
        PostResponse post1 = createPost(1L, "Dependency Injection", "DI를 설명합니다",
                LocalDateTime.of(2024, 2, 9, 20, 22));
        PostResponse post2 = createPost(2L, "AOP", "AOP를 설명합니다",
                LocalDateTime.of(2024, 2, 9, 20, 25));

        PostListResponse response = PostListResponse.of("전체 글 목록", "", List.of(post1, post2));

        given(postService.findAll(any())).willReturn(response);

        // when // then
        mockMvc.perform(get("/api/v1/posts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-list",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").optional().description("데이터 수")
                        ),
                        responseFields(
                                fieldWithPath("categoryName").type(JsonFieldType.STRING)
                                        .description("카테고리 이름"),
                                fieldWithPath("categoryDescription").type(JsonFieldType.STRING)
                                        .description("카테고리 설명"),
                                fieldWithPath("posts[]").type(JsonFieldType.ARRAY)
                                        .description("게시글 목록"),
                                fieldWithPath("posts[].postId").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("posts[].title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("posts[].description").type(JsonFieldType.STRING)
                                        .description("게시글 설명"),
                                fieldWithPath("posts[].createdAt").type(JsonFieldType.STRING)
                                        .description("게시글 생성 시각")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 단건 상세 조회 API")
    void getPost() throws Exception {
        // given
        PostDetailResponse response = PostDetailResponse.of(
                1L, "Dependency Injection", "DI를 설명합니다",
                "DI는 의존성을 주입하여 실제 구현체를 감추는 기술로..[더보기]",
                LocalDateTime.of(2024, 2, 9, 20, 22));
        given(postService.findByTitle(any())).willReturn(response);

        // when // then
        mockMvc.perform(get("/api/v1/posts/{title}", response.title()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-detail",
                        pathParameters(
                                parameterWithName("title").description("게시글 제목")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("게시글 설명"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("게시글 본문"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                        .description("게시글 생성 시각")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 별 게시글 목록 조회 API")
    void getPostsByCategory() throws Exception {
        // given
        PostResponse post1 = createPost(1L, "Dependency Injection", "DI를 설명합니다",
                LocalDateTime.of(2024, 2, 9, 20, 22));
        PostResponse post2 = createPost(2L, "AOP", "AOP를 설명합니다",
                LocalDateTime.of(2024, 2, 9, 20, 25));
        PostResponse post3 = createPost(3L, "GC", "GC를 설명합니다",
                LocalDateTime.of(2024, 2, 9, 20, 54));

        PostListResponse response = PostListResponse.of("Backend", "백엔드를 설명합니다",
                List.of(post1, post2, post3));

        given(postService.findByCategory(any(), any())).willReturn(response);

        // when // then
        mockMvc.perform(get("/api/v1/category/{categoryName}/posts", response.categoryName())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-category-list",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").optional().description("데이터 수")
                        ),
                        pathParameters(
                                parameterWithName("categoryName").description("카테고리 이름")
                        ),
                        responseFields(
                                fieldWithPath("categoryName").type(JsonFieldType.STRING)
                                        .description("카테고리 이름"),
                                fieldWithPath("categoryDescription").type(JsonFieldType.STRING)
                                        .description("카테고리 설명"),
                                fieldWithPath("posts[]").type(JsonFieldType.ARRAY)
                                        .description("게시글 목록"),
                                fieldWithPath("posts[].postId").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("posts[].title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("posts[].description").type(JsonFieldType.STRING)
                                        .description("게시글 설명"),
                                fieldWithPath("posts[].createdAt").type(JsonFieldType.STRING)
                                        .description("게시글 생성 시각")
                        )
                ));
    }

    @Test
    @DisplayName("세부 카테고리 별 게시글 목록 조회 API")
    void getPostsByCategoryItem() throws Exception {
        // given
        PostResponse post1 = createPost(1L, "Dependency Injection", "DI를 설명합니다",
                LocalDateTime.of(2024, 2, 9, 20, 22));
        PostResponse post2 = createPost(2L, "AOP", "AOP를 설명합니다",
                LocalDateTime.of(2024, 2, 9, 20, 25));

        PostListResponse response = PostListResponse.of("Spring", "Spring을 설명합니다",
                List.of(post1, post2));

        given(postService.findByCategoryItem(any(), any())).willReturn(response);

        // when // then
        mockMvc.perform(get("/api/v1/categoryItem/{itemName}/posts", response.categoryName())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-categoryItem-list",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").optional().description("데이터 수")
                        ),
                        pathParameters(
                                parameterWithName("itemName").description("세부 카테고리 이름")
                        ),
                        responseFields(
                                fieldWithPath("categoryName").type(JsonFieldType.STRING)
                                        .description("카테고리 이름"),
                                fieldWithPath("categoryDescription").type(JsonFieldType.STRING)
                                        .description("카테고리 설명"),
                                fieldWithPath("posts[]").type(JsonFieldType.ARRAY)
                                        .description("게시글 목록"),
                                fieldWithPath("posts[].postId").type(JsonFieldType.NUMBER)
                                        .description("게시글 ID"),
                                fieldWithPath("posts[].title").type(JsonFieldType.STRING)
                                        .description("게시글 제목"),
                                fieldWithPath("posts[].description").type(JsonFieldType.STRING)
                                        .description("게시글 설명"),
                                fieldWithPath("posts[].createdAt").type(JsonFieldType.STRING)
                                        .description("게시글 생성 시각")
                        )
                ));
    }

    private PostResponse createPost(Long postId, String title, String description, LocalDateTime createdAt) {
        return PostResponse.builder()
                .postId(postId)
                .title(title)
                .description(description)
                .createdAt(createdAt)
                .build();
    }
}