package ca.metricalsky.yt.comments.mapper.entity;

import ca.metricalsky.yt.comments.entity.Channel;
import ca.metricalsky.yt.comments.mapper.OffsetDateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Mapper(uses = OffsetDateTimeMapper.class)
public abstract class ChannelMapper {

    private static final Pattern KEYWORD_PATTERN = Pattern.compile("\".+?\"|[^ ]+");

    @Mapping(target = "title", source = "snippet.title")
    @Mapping(target = "description", source = "snippet.description")
    @Mapping(target = "customUrl", source = "snippet.customUrl")
    @Mapping(target = "thumbnailUrl", source = "snippet.thumbnails.high.url")
    @Mapping(target = "publishedAt", source = "snippet.publishedAt")
    @Mapping(target = "topics", source = "topicDetails.topicCategories")
    @Mapping(target = "keywords", source = "brandingSettings.channel.keywords")
    @Mapping(target = "lastFetchedAt", ignore = true)
    public abstract Channel fromYouTube(com.google.api.services.youtube.model.Channel ytChannel);

    Set<String> fromYouTubeKeywords(String ytKeywords) {
        return ytKeywords == null ? null : KEYWORD_PATTERN.matcher(ytKeywords).results()
                .map(MatchResult::group)
                .map(this::trimQuotes)
                .collect(Collectors.toSet());
    }

    private String trimQuotes(String keyword) {
        return keyword.startsWith("\"") && keyword.endsWith("\"")
                ? keyword.substring(1, keyword.length() - 1)
                : keyword;
    }
}
