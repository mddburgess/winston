package ca.metricalsky.winston.web;

import ca.metricalsky.winston.config.AppResourceResolver;
import ca.metricalsky.winston.dto.author.AuthorDto;
import ca.metricalsky.winston.dto.CommentDto;
import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.service.AuthorService;
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
import java.util.Set;

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
    void findAuthorDetails() throws Exception {
        when(authorService.findByHandle(AUTHOR_DISPLAY_NAME))
                .thenReturn(Optional.of(buildAuthorDto()));
        when(commentService.findAllWithContextByAuthorId(AUTHOR_ID))
                .thenReturn(List.of(buildCommentDto()));
        when(videoService.getAllById(Set.of(VIDEO_ID)))
                .thenReturn(List.of(buildVideoDto()));

        mvc.perform(get("/api/v1/authors/{authorHandle}", AUTHOR_DISPLAY_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author.id").value(AUTHOR_ID))
                .andExpect(jsonPath("$.author.displayName").value(AUTHOR_DISPLAY_NAME))
                .andExpect(jsonPath("$.comments", hasSize(1)))
                .andExpect(jsonPath("$.comments[0].id").value("comment.id"))
                .andExpect(jsonPath("$.comments[0].videoId").value(VIDEO_ID))
                .andExpect(jsonPath("$.comments[0].text").value("comment.text"))
                .andExpect(jsonPath("$.videos", hasSize(1)))
                .andExpect(jsonPath("$.videos[0].id").value("video.id"))
                .andExpect(jsonPath("$.videos[0].title").value("video.title"));
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

    private static CommentDto buildCommentDto() {
        var commentDto = new CommentDto();
        commentDto.setId("comment.id");
        commentDto.setVideoId(VIDEO_ID);
        commentDto.setText("comment.text");
        return commentDto;
    }

    private static VideoDto buildVideoDto() {
        var videoDto = new VideoDto();
        videoDto.setId(VIDEO_ID);
        videoDto.setTitle("video.title");
        return videoDto;
    }
}
