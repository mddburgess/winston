package ca.metricalsky.winston.web;

import ca.metricalsky.winston.dto.author.AuthorDetailsResponse;
import ca.metricalsky.winston.dto.CommentDto;
import ca.metricalsky.winston.dto.author.AuthorListResponse;
import ca.metricalsky.winston.service.AuthorService;
import ca.metricalsky.winston.service.CommentService;
import ca.metricalsky.winston.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final CommentService commentService;
    private final VideoService videoService;

    @GetMapping
    public AuthorListResponse list() {
        var authors = authorService.findAll();
        return new AuthorListResponse(authors);
    }

    @GetMapping("/{authorHandle}")
    public AuthorDetailsResponse findAuthorDetails(@PathVariable String authorHandle) {

        var authorDto = authorService.findByHandle(authorHandle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var commentDtos = commentService.findAllWithContextByAuthorId(authorDto.getId());

        var videoIds = commentDtos.stream()
                .map(CommentDto::getVideoId)
                .collect(Collectors.toSet());
        var videoDtos = videoService.getAllById(videoIds);

        return AuthorDetailsResponse.builder()
                .author(authorDto)
                .comments(commentDtos)
                .videos(videoDtos)
                .build();
    }
}
