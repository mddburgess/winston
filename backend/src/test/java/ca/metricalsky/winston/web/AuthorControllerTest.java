package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.model.Author;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.config.AppResourceResolver;
import ca.metricalsky.winston.dao.AuthorDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.test.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest({AuthorController.class, AppResourceResolver.class})
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthorDataService authorDataService;
    @MockitoBean
    private VideoDataService videoDataService;

    @Test
    void listAuthors() throws Exception {
        var author = buildAuthor();

        when(authorDataService.getAllAuthors())
                .thenReturn(List.of(author));

        mvc.perform(get("/api/v1/authors")).andExpectAll(
                status().isOk(),
                jsonPath("$.results").value(1),
                jsonPath("$.authors").isNotEmpty(),
                jsonPath("$.authors[0].id").value(author.getId()),
                jsonPath("$.authors[0].handle").value(author.getHandle())
        );
    }

    @Test
    void listAuthors_noResults() throws Exception {
        when(authorDataService.getAllAuthors())
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/authors")).andExpectAll(
                status().isOk(),
                jsonPath("$.results").value(0),
                jsonPath("$.authors").isEmpty()
        );
    }

    @Test
    void getAuthor() throws Exception {
        var author = buildAuthor();
        var video = buildVideo();

        when(authorDataService.findAuthorByHandle(author.getHandle()))
                .thenReturn(Optional.of(author));
        when(videoDataService.getVideosForAuthor(author.getHandle()))
                .thenReturn(List.of(video));

        mvc.perform(get("/api/v1/authors/{handle}", author.getHandle())).andExpectAll(
                status().isOk(),
                jsonPath("$.author.id").value(author.getId()),
                jsonPath("$.author.handle").value(author.getHandle()),
                jsonPath("$.videos").isNotEmpty(),
                jsonPath("$.videos[0].id").value(video.getId())
        );
    }

    @Test
    void getAuthor_noVideos() throws Exception {
        var author = buildAuthor();

        when(authorDataService.findAuthorByHandle(author.getHandle()))
                .thenReturn(Optional.of(author));
        when(videoDataService.getVideosForAuthor(author.getHandle()))
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/authors/{handle}", author.getHandle())).andExpectAll(
                status().isOk(),
                jsonPath("$.author.id").value(author.getId()),
                jsonPath("$.author.handle").value(author.getHandle()),
                jsonPath("$.videos").isEmpty()
        );
    }

    @Test
    void getAuthor_notFound() throws Exception {
        var handle = TestUtils.randomString();

        when(authorDataService.findAuthorByHandle(handle))
                .thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/authors/{handle}", handle)).andExpectAll(
                status().isNotFound(),
                jsonPath("$.type").value("about:blank"),
                jsonPath("$.title").value(HttpStatus.NOT_FOUND.getReasonPhrase()),
                jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                jsonPath("$.detail").value("The requested author was not found."),
                jsonPath("$.instance").exists()
        );

        verifyNoInteractions(videoDataService);
    }

    private static Author buildAuthor() {
        return new Author()
                .id(TestUtils.randomId())
                .handle(TestUtils.randomString());
    }

    private static Video buildVideo() {
        return new Video()
                .id(TestUtils.randomId());
    }
}
