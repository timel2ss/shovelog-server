package me.shovelog;

import me.shovelog.category.controller.CategoryController;
import me.shovelog.category.service.CategoryService;
import me.shovelog.post.controller.PostController;
import me.shovelog.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@ActiveProfiles("test")
@WebMvcTest(controllers = {CategoryController.class, PostController.class})
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocs {
    protected MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext context,
               RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(provider)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @MockBean
    protected CategoryService categoryService;

    @MockBean
    protected PostService postService;
}
