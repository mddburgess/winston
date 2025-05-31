package ca.metricalsky.winston.web;

import ca.metricalsky.winston.config.AppResourceResolver;
import ca.metricalsky.winston.dto.author.AuthorDto;
import ca.metricalsky.winston.service.AuthorService;
import ca.metricalsky.winston.service.ChannelService;
import ca.metricalsky.winston.service.CommentService;
import ca.metricalsky.winston.service.VideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

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
    private static final String VIDEO_ID = "video.id";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthorService authorService;
    @MockitoBean
    private ChannelService channelService;
    @MockitoBean
    private CommentService commentService;
    @MockitoBean
    private VideoService videoService;

    @Test
    void list() throws Exception {
        when(authorService.findAll())
                .thenReturn(List.of(buildAuthorDto()));

        mvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").value(1))
                .andExpect(jsonPath("$.authors", hasSize(1)))
                .andExpect(jsonPath("$.authors[0].id").value(AUTHOR_ID));
    }

    @Test
    void list_noResults() throws Exception {
        when(authorService.findAll())
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").value(0))
                .andExpect(jsonPath("$.authors", hasSize(0)));
    }

    @Test
    void findAuthorDetails_authorNotFound() throws Exception {
        when(authorService.findByHandle(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/authors/{authorHandle}", AUTHOR_DISPLAY_NAME))
                .andExpect(status().isNotFound());
    }

    private static AuthorDto buildAuthorDto() {
        var authorDto = new AuthorDto();
        authorDto.setId(AUTHOR_ID);
        authorDto.setDisplayName(AUTHOR_DISPLAY_NAME);
        return authorDto;
    }
}
