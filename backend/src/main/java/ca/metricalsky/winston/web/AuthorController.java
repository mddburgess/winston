package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.AuthorsApi;
import ca.metricalsky.winston.api.model.GetAuthorResponse;
import ca.metricalsky.winston.api.model.ListAuthorsResponse;
import ca.metricalsky.winston.dao.AuthorDataService;
import ca.metricalsky.winston.dao.VideoDataService;
import ca.metricalsky.winston.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorController implements AuthorsApi {

    private final AuthorDataService authorDataService;
    private final VideoDataService videoDataService;

    @Override
    public ResponseEntity<ListAuthorsResponse> listAuthors() {
        var authors = authorDataService.getAllAuthors();
        var response = new ListAuthorsResponse()
                .results(authors.size())
                .authors(authors);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetAuthorResponse> getAuthor(String handle) {
        var author = authorDataService.findAuthorByHandle(handle)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "The requested author was not found."));
        var videos = videoDataService.getVideosForAuthor(handle);
        var response = new GetAuthorResponse()
                .author(author)
                .videos(videos);

        return ResponseEntity.ok(response);
    }
}
