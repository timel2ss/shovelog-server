package me.shovelog.exception;

import me.shovelog.RestDocs;
import me.shovelog.exception.notfound.CategoryNotFoundException;
import me.shovelog.exception.notfound.PostNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExceptionControllerTest extends RestDocs {

    @Test
    @DisplayName("게시글 단건 상세 조회 API - PostNotFoundException")
    void postNotFound() throws Exception {
        // given
        given(postService.findByTitle(any())).willThrow(new PostNotFoundException());

        // when // then
        mockMvc.perform(get("/api/v1/posts/{title}", "Non-Existing-Post-Title"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value(PostNotFoundException.MESSAGE))
                .andDo(print())
                .andDo(document("exception-post-not-found",
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("예외 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 별 게시글 목록 조회 API - CategoryNotFoundException")
    void getPostsByCategory() throws Exception {
        // given
        given(postService.findByCategory(any(), any())).willThrow(new CategoryNotFoundException());

        // when // then
        mockMvc.perform(get("/api/v1/category/{categoryName}/posts", "Non-Existing-Category-Name")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value(CategoryNotFoundException.MESSAGE))
                .andDo(print())
                .andDo(document("exception-category-not-found",
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("예외 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("세부 카테고리 별 게시글 목록 조회 API - CategoryNotFoundException")
    void getPostsByCategoryItem() throws Exception {
        // given
        given(postService.findByCategoryItem(any(), any())).willThrow(new CategoryNotFoundException());

        // when // then
        mockMvc.perform(get("/api/v1/categoryItem/{itemName}/posts", "Non-Existing-CategoryItem-Name")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value(CategoryNotFoundException.MESSAGE))
                .andDo(print())
                .andDo(document("exception-categoryItem-not-found",
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("HTTP 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("예외 메시지")
                        )
                ));
    }
}