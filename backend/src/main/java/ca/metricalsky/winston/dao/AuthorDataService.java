package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Author;
import ca.metricalsky.winston.api.model.AuthorStatistics;
import ca.metricalsky.winston.api.model.VideoStatistics;
import ca.metricalsky.winston.mappers.api.AuthorMapper;
import ca.metricalsky.winston.repository.AuthorDetailsRepository;
import ca.metricalsky.winston.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorDataService {

    private static final String CHANNEL_URL_PREFIX = "http://www.youtube.com/c/";

    private final AuthorDetailsRepository audioDetailsRepository;
    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;
    private final ConversionService conversionService;

    public long countAuthors() {
        return authorRepository.count();
    }

    public List<Author> listAuthors(PageRequest page) {
        var pageRequest = page.withSort(Sort.Direction.ASC, "displayName");

        return audioDetailsRepository.findAuthorDetailsPage(pageRequest)
                .stream()
                .map(authorMapper::toAuthor)
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
