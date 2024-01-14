package me.shovelog.category.controller;

import me.shovelog.RestDocs;
import me.shovelog.category.service.response.CategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends RestDocs {

    @Test
    @DisplayName("카테고리 목록 조회 API")
    void getCategories() throws Exception {
        // given
        List<CategoryResponse> response = List.of(
                createCategory(1L,
                        "프론트엔드",
                        "웹 프론트엔드 개발 일기"),
                createCategory(2L,
                        "백엔드",
                        "백엔드 개발 일기"),
                createCategory(3L,
                        "데이터베이스",
                        "데이터베이스 학습 일기")
        );
        given(categoryService.findAll()).willReturn(response);

        // when then
        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("category-list",
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                        .description("카테고리 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING)
                                        .description("카테고리 이름"),
                                fieldWithPath("[].description").type(JsonFieldType.STRING)
                                        .description("카테고리 설명")
                        )
                ));
    }

    private CategoryResponse createCategory(long id,
                                            String name,
                                            String description) {
        return CategoryResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }
}