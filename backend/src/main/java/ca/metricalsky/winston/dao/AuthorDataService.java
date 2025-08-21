package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Author;
import ca.metricalsky.winston.api.model.AuthorStatistics;
import ca.metricalsky.winston.api.model.VideoStatistics;
import ca.metricalsky.winston.mappers.api.AuthorMapper;
import ca.metricalsky.winston.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
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
    private final ConversionService conversionService;

    public List<Author> getAllAuthors() {
        return authorRepository.findAllAuthorDetails()
                .stream()
                .map(authorMapper::toAuthor)
                .sorted(Comparator.comparing(Author::getHandle))
                .toList();
    }

    public Optional<Author> findAuthorByHandle(String handle) {
        var maybeAuthor = Optionals.firstNonEmpty(
                () -> authorRepository.findByDisplayName(handle),
                () -> authorRepository.findByChannelUrl(getChannelUrl(handle)),
                () -> authorRepository.findById(handle)
        ).map(authorMapper::toAuthor);

        maybeAuthor.ifPresent(author -> {
            var videoStatistics = authorRepository.findVideoStatisticsByAuthorId(author.getId())
                    .stream()
                    .map(entity -> conversionService.convert(entity, VideoStatistics.class))
                    .toList();

            author.setVideoStatistics(videoStatistics);
            author.setAuthorStatistics(conversionService.convert(videoStatistics, AuthorStatistics.class));
        });

        return maybeAuthor;
    }

    private static String getChannelUrl(String authorHandle) {
        return CHANNEL_URL_PREFIX + URLEncoder.encode(authorHandle, StandardCharsets.UTF_8);
    }
}
