package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Author;
import ca.metricalsky.winston.api.model.AuthorStatistics;
import ca.metricalsky.winston.mappers.api.AuthorMapper;
import ca.metricalsky.winston.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorDataService {

    private static final String CHANNEL_URL_PREFIX = "http://www.youtube.com/c/";

    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAllAuthorDetails()
                .stream()
                .map(authorMapper::toAuthor)
                .sorted(Comparator.comparing(Author::getHandle))
                .toList();
    }

    public Optional<Author> findAuthorByHandle(String handle) {
        var author = Optionals.firstNonEmpty(
                () -> authorRepository.findByDisplayName(handle),
                () -> authorRepository.findByChannelUrl(getChannelUrl(handle)),
                () -> authorRepository.findById(handle)
        ).map(authorMapper::toAuthor).orElse(null);

        if (author == null) {
            return Optional.empty();
        }

        authorRepository.findAuthorDetailsById(author.getId()).ifPresent(authorDetails -> {
            var authorStatistics = new AuthorStatistics()
                    .channelCount(authorDetails.getChannelCount().intValue())
                    .videoCount(authorDetails.getVideoCount().intValue())
                    .commentCount(authorDetails.getCommentCount().intValue())
                    .replyCount(authorDetails.getReplyCount().intValue());
            author.setStatistics(authorStatistics);
        });

        return Optional.of(author);
    }

    private static String getChannelUrl(String authorHandle) {
        return CHANNEL_URL_PREFIX + URLEncoder.encode(authorHandle, StandardCharsets.UTF_8);
    }
}
