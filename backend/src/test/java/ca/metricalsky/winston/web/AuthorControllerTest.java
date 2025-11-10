package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.model.Author;
import ca.metricalsky.winston.api.model.Video;
import ca.metricalsky.winston.config.properties.api.AuthorsApiConfig;
import ca.metricalsky.winston.dao.AuthorDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.test.TestUtils;
import ca.metricalsky.winston.test.annotations.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(AuthorController.class)
@EnableConfigurationProperties(AuthorsApiConfig.class)
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

        when(authorDataService.countAuthors(null))
                .thenReturn(1L);
        when(authorDataService.searchAuthors(eq(null), any()))
                .thenReturn(List.of(author));

        mvc.perform(get("/api/v1/authors")).andExpectAll(
                status().isOk(),
                jsonPath("$.results.page_count").value(1),
                jsonPath("$.results.total_count").value(1),
                jsonPath("$.authors", hasSize(1)),
                jsonPath("$.authors[0].id").value(author.getId()),
                jsonPath("$.authors[0].handle").value(author.getHandle())
        );
    }

    @Test
    void listAuthors_noResults() throws Exception {
        when(authorDataService.countAuthors(null))
                .thenReturn(0L);
        when(authorDataService.searchAuthors(eq(null), any()))
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/authors")).andExpectAll(
                status().isOk(),
                jsonPath("$.results.page_count").value(0),
                jsonPath("$.results.total_count").value(0),
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
                jsonPath("$.videos", hasSize(1)),
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
                jsonPath("$.type").value("/api/problem/author-not-found"),
                jsonPath("$.title").value(HttpStatus.NOT_FOUND.getReasonPhrase()),
                jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                jsonPath("$.detail").value(ErrorCode.AUTHOR_NOT_FOUND.getDetail()),
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
