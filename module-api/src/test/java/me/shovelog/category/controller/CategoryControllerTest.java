package me.shovelog.category.controller;

import me.shovelog.RestDocs;
import me.shovelog.category.service.response.CategoryItemResponse;
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
        CategoryItemResponse categoryItem1 = createCategoryItem(1L, "React", "리액트 설명");
        CategoryItemResponse categoryItem2 = createCategoryItem(2L, "Vue", "뷰 설명");
        CategoryItemResponse categoryItem3 = createCategoryItem(3L, "Spring", "스프링 설명");
        CategoryItemResponse categoryItem4 = createCategoryItem(4L, "JVM", "JVM 설명");
        CategoryItemResponse categoryItem5 = createCategoryItem(5L, "NestJS", "NestJS 설명");
        CategoryItemResponse categoryItem6 = createCategoryItem(6L, "DBMS", "DBMS 설명");
        CategoryItemResponse categoryItem7 = createCategoryItem(7L, "SQL", "SQL 설명");

        List<CategoryResponse> response = List.of(
                createCategory(1L,
                        "프론트엔드",
                        "웹 프론트엔드 개발 일기",
                        List.of(categoryItem1, categoryItem2)),
                createCategory(2L,
                        "백엔드",
                        "백엔드 개발 일기",
                        List.of(categoryItem3, categoryItem4, categoryItem5)),
                createCategory(3L,
                        "데이터베이스",
                        "데이터베이스 학습 일기",
                        List.of(categoryItem6, categoryItem7)));
        given(categoryService.findAll()).willReturn(response);

        // when then
        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("category-list",
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                        .description("카테고리 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                        .description("카테고리 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING)
                                        .description("카테고리 이름"),
                                fieldWithPath("[].description").type(JsonFieldType.STRING)
                                        .description("카테고리 설명"),
                                fieldWithPath("[].items[]").type(JsonFieldType.ARRAY)
                                        .description("카테고리 아이템 목록"),
                                fieldWithPath("[].items[].id").type(JsonFieldType.NUMBER)
                                        .description("카테고리 아이템 ID"),
                                fieldWithPath("[].items[].itemName").type(JsonFieldType.STRING)
                                        .description("카테고리 아이템 이름"),
                                fieldWithPath("[].items[].description").type(JsonFieldType.STRING)
                                        .description("카테고리 아이템 설명")
                        )
                ));
    }

    private CategoryResponse createCategory(long id,
                                            String name,
                                            String description,
                                            List<CategoryItemResponse> items) {
        return CategoryResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .items(items)
                .build();
    }

    private CategoryItemResponse createCategoryItem(long id,
                                                    String itemName,
                                                    String description) {
        return CategoryItemResponse.builder()
                .id(id)
                .itemName(itemName)
                .description(description)
                .build();
    }
}