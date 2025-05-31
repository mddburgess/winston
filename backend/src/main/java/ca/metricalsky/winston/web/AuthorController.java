package ca.metricalsky.winston.web;

import ca.metricalsky.winston.dto.VideoDto;
import ca.metricalsky.winston.dto.author.AuthorListResponse;
import ca.metricalsky.winston.dto.author.AuthorSummaryResponse;
import ca.metricalsky.winston.service.AuthorService;
import ca.metricalsky.winston.service.ChannelService;
import ca.metricalsky.winston.service.CommentService;
import ca.metricalsky.winston.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthorController {

    private final AuthorService authorService;
    private final ChannelService channelService;
    private final CommentService commentService;
    private final VideoService videoService;

    @GetMapping("/api/v1/authors")
    public AuthorListResponse list() {
        var authors = authorService.findAll();
        return new AuthorListResponse(authors);
    }

    @GetMapping("/api/v2/authors/{authorHandle}")
    public AuthorSummaryResponse findAuthorSummary(@PathVariable String authorHandle) {
        var authorDto = authorService.findByHandle(authorHandle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var videoDtos = videoService.findAllByAuthorHandle(authorHandle);
        var channelIds = videoDtos.stream()
                .map(VideoDto::getChannelId)
                .toList();
        var channelDtos = channelService.findAllById(channelIds);

        return new AuthorSummaryResponse(authorDto, channelDtos, videoDtos);
    }
}
