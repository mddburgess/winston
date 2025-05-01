package ca.metricalsky.winston.service;

import ca.metricalsky.winston.dto.author.AuthorDto;
import ca.metricalsky.winston.mapper.dto.AuthorDtoMapper;
import ca.metricalsky.winston.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private static final String CHANNEL_URL_PREFIX = "http://www.youtube.com/c/";

    private final AuthorDtoMapper authorDtoMapper = Mappers.getMapper(AuthorDtoMapper.class);
    private final AuthorRepository authorRepository;

    public List<AuthorDto> findAll() {
        return authorRepository.findAllAuthorDetails()
                .stream()
                .map(authorDtoMapper::toAuthorDto)
                .sorted(Comparator.comparing(AuthorDto::getDisplayName))
                .toList();
    }

    public Optional<AuthorDto> findByHandle(String authorHandle) {
        return Optionals.firstNonEmpty(
                () -> authorRepository.findByDisplayName(authorHandle),
                () -> authorRepository.findByChannelUrl(getChannelUrl(authorHandle)),
                () -> authorRepository.findById(authorHandle)
        ).map(authorDtoMapper::toAuthorDto);
    }

    private static String getChannelUrl(String authorHandle) {
        return CHANNEL_URL_PREFIX + URLEncoder.encode(authorHandle, StandardCharsets.UTF_8);
    }
}
