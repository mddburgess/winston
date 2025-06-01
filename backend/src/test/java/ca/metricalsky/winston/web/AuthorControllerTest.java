package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.model.Author;
import ca.metricalsky.winston.config.AppResourceResolver;
import ca.metricalsky.winston.dao.AuthorDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest({AuthorController.class, AppResourceResolver.class})
class AuthorControllerTest {

    private static final String AUTHOR_DISPLAY_NAME = "author.displayName";
    private static final String AUTHOR_ID = "author.id";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthorDataService authorDataService;
    @MockitoBean
    private VideoDataService videoDataService;

    @Test
    void list() throws Exception {
        when(authorDataService.getAllAuthors())
                .thenReturn(List.of(buildAuthor()));

        mvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").value(1))
                .andExpect(jsonPath("$.authors", hasSize(1)))
                .andExpect(jsonPath("$.authors[0].id").value(AUTHOR_ID));
    }

    @Test
    void list_noResults() throws Exception {
        when(authorDataService.getAllAuthors())
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").value(0))
                .andExpect(jsonPath("$.authors", hasSize(0)));
    }

    private static Author buildAuthor() {
        return new Author()
                .id(AUTHOR_ID)
                .handle(AUTHOR_DISPLAY_NAME);
    }
}
