package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.AuthorsApi;
import ca.metricalsky.winston.api.model.GetAuthorResponse;
import ca.metricalsky.winston.api.model.ListAuthorsResponse;
import ca.metricalsky.winston.api.model.ListVideosResponseResults;
import ca.metricalsky.winston.config.properties.api.AuthorsApiConfig;
import ca.metricalsky.winston.dao.AuthorDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@RestController
@RequiredArgsConstructor
public class AuthorController implements AuthorsApi {

    private final AuthorDataService authorDataService;
    private final VideoDataService videoDataService;
    private final AuthorsApiConfig config;

    @Override
    public ResponseEntity<ListAuthorsResponse> listAuthors(String search, Integer page, Integer size) {
        var pageRequest = PageRequest.of(defaultIfNull(page, 0), defaultIfNull(size, config.getDefaultPageSize()));

        var totalCount = (int) authorDataService.countAuthors(search);
        var authors = authorDataService.searchAuthors(search, pageRequest);

        var response = new ListAuthorsResponse()
                .results(new ListVideosResponseResults()
                        .pageCount(authors.size())
                        .totalCount(totalCount))
                .authors(authors);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetAuthorResponse> getAuthor(String handle) {
        var author = authorDataService.findAuthorByHandle(handle)
                .orElseThrow(() -> new AppException(ErrorCode.AUTHOR_NOT_FOUND));
        var videos = videoDataService.getVideosForAuthor(handle);
        var response = new GetAuthorResponse()
                .author(author)
                .videos(videos);

        return ResponseEntity.ok(response);
    }
}
