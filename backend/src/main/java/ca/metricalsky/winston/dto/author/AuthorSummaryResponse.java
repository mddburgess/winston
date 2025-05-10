package ca.metricalsky.winston.dto.author;

import ca.metricalsky.winston.dto.ChannelDto;
import ca.metricalsky.winston.dto.VideoDto;

import java.util.List;

public record AuthorSummaryResponse(
        AuthorDto author,
        List<ChannelDto> channels,
        List<VideoDto> videos
) {
}
