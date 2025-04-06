package ca.metricalsky.winston.dto.author;

import java.util.List;

public record AuthorListResponse(
        int results,
        List<AuthorDto> authors
) {

    public AuthorListResponse(List<AuthorDto> items) {
        this(items.size(), items);
    }
}
