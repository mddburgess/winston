package ca.metricalsky.yt.comments.mapper;

import ca.metricalsky.yt.comments.entity.Channel;
import ca.metricalsky.yt.comments.entity.Keyword;
import ca.metricalsky.yt.comments.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Mapper(uses = OffsetDateTimeMapper.class)
public abstract class ChannelMapper {

    private static final Pattern KEYWORD_PATTERN = Pattern.compile("\".+?\"|[^ ]+");

    @Mapping(target = "title", source = "snippet.title")
    @Mapping(target = "description", source = "snippet.description")
    @Mapping(target = "customUrl", source = "snippet.customUrl")
    @Mapping(target = "publishedAt", source = "snippet.publishedAt")
    @Mapping(target = "thumbnailUrl", source = "snippet.thumbnails.default.url")
    @Mapping(target = "topics", source = "topicDetails.topicCategories")
    @Mapping(target = "keywords", source = "brandingSettings.channel.keywords")
    public abstract Channel fromYouTube(com.google.api.services.youtube.model.Channel ytChannel);

    @Mapping(target = "topicUrl", source = ".")
    abstract Topic fromYouTubeTopicCategory(String topicCategory);

    List<Keyword> fromYouTubeKeywords(String ytKeywords) {
        return KEYWORD_PATTERN.matcher(ytKeywords).results()
                .map(MatchResult::group)
                .map(this::fromYouTubeKeyword)
                .toList();
    }

    @Mapping(target = "name", source = ".")
    abstract Keyword fromYouTubeKeyword(String ytKeyword);
}
