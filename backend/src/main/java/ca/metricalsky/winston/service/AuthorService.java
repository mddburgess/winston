package ca.metricalsky.winston.service;

import ca.metricalsky.winston.dto.author.AuthorDto;
import ca.metricalsky.winston.mapper.dto.AuthorDtoMapper;
import ca.metricalsky.winston.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorDtoMapper authorDtoMapper = Mappers.getMapper(AuthorDtoMapper.class);
    private final AuthorRepository authorRepository;

    public List<AuthorDto> findAll() {
        return authorRepository.findAllAuthorDetails()
                .stream()
                .map(authorDtoMapper::toAuthorDto)
                .sorted(Comparator.comparing(AuthorDto::getDisplayName))
                .toList();
    }

    public Optional<AuthorDto> findById(String authorId) {
        return authorRepository.findById(authorId)
                .map(authorDtoMapper::toAuthorDto);
    }
}
