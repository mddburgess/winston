package ca.metricalsky.winston.dao;

import ca.metricalsky.winston.api.model.Author;
import ca.metricalsky.winston.api.model.AuthorChannel;
import ca.metricalsky.winston.api.model.AuthorStatistics;
import ca.metricalsky.winston.api.model.AuthorVideo;
import ca.metricalsky.winston.api.model.PatchOperation;
import ca.metricalsky.winston.api.model.VideoStatistics;
import ca.metricalsky.winston.database.repository.author.AuthorRepository;
import ca.metricalsky.winston.database.view.AuthorDetailsView;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.mappers.api.AuthorMapper;
import ca.metricalsky.winston.utils.JsonPatchUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorDataService {

    private static final String CHANNEL_URL_PREFIX = "http://www.youtube.com/c/";

    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;
    private final ConversionService conversionService;
    private final JsonPatchUtils jsonPatchUtils;

    public long countAuthors(String search) {
        return StringUtils.isNotBlank(search)
                ? authorRepository.countByDisplayNameLike("%" + search + "%")
                : authorRepository.count();
    }

    public List<Author> searchAuthors(String search, PageRequest page) {
        var pageRequest = page.withSort(Sort.Direction.ASC, "displayName");

        var authorEntities = StringUtils.isNotBlank(search)
                ? authorRepository.findAllByDisplayNameLike("%" + search + "%", pageRequest)
                : authorRepository.findAll(pageRequest);
        var authors = authorEntities.stream()
                .map(authorMapper::toAuthor)
                .toList();
        var authorIds = authors.stream()
                .map(Author::getId)
                .toList();
        var authorStatistics = authorRepository.findAuthorDetailsByIds(authorIds)
                .stream()
                .collect(Collectors.toMap(AuthorDetailsView::getAuthorId,
                        details -> conversionService.convert(details, AuthorStatistics.class)));

        return authors.stream()
                .map(author -> author.authorStatistics(authorStatistics.get(author.getId())))
                .toList();
    }

    public Optional<Author> findAuthorByHandle(String handle) {
        var maybeAuthor = Optional.ofNullable(ObjectUtils.getFirstNonNull(
                () -> authorRepository.findByDisplayName(handle),
                () -> authorRepository.findByChannelUrl(getChannelUrl(handle)),
                () -> authorRepository.getReferenceById(handle)
        )).map(authorMapper::toAuthor);

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

    public List<AuthorChannel> findAuthorChannelsByHandle(String handle) {
        return authorRepository.findAuthorChannelsByDisplayName(handle)
                .stream()
                .map(authorChannel -> conversionService.convert(authorChannel, AuthorChannel.class))
                .toList();
    }

    public List<AuthorVideo> findAuthorVideosByHandle(String handle) {
        return authorRepository.findAuthorVideosByDisplayName(handle)
                .stream()
                .map(authorVideo -> conversionService.convert(authorVideo, AuthorVideo.class))
                .toList();
    }

    public Author patchAuthor(String handle, List<PatchOperation> patchOperations) {
        var author = authorRepository.findByDisplayName(handle);
        if (author == null) {
            throw new AppException(ErrorCode.AUTHOR_NOT_FOUND);
        }

        var patchedAuthor = jsonPatchUtils.applyPatch(author, patchOperations);
        patchedAuthor = authorRepository.save(patchedAuthor);

        return authorMapper.toAuthor(patchedAuthor);
    }

    private static String getChannelUrl(String authorHandle) {
        return CHANNEL_URL_PREFIX + URLEncoder.encode(authorHandle, StandardCharsets.UTF_8);
    }
}
