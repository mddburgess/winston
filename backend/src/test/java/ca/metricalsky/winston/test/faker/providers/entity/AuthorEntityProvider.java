package ca.metricalsky.winston.test.faker.providers.entity;

import ca.metricalsky.winston.entity.view.AuthorDetailsView;
import ca.metricalsky.winston.test.faker.providers.YoutubeProvider;
import lombok.Builder;
import lombok.Value;
import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

public class AuthorEntityProvider extends AbstractProvider<BaseProviders> {

    private final YoutubeProvider youtube;

    public AuthorEntityProvider(BaseProviders faker) {
        super(faker);
        youtube = new YoutubeProvider(faker);
    }

    public AuthorDetailsView authorDetails() {
        var totalCommentCount = faker.number().numberBetween(1L, 1000);

        return MockAuthorDetailsView.builder()
                .authorId(youtube.authorId())
                .channelCount(faker.number().numberBetween(1L, 20))
                .videoCount(faker.number().numberBetween(1L, 100))
                .totalCommentCount(totalCommentCount)
                .replyCount(faker.number().numberBetween(0L, totalCommentCount - 1))
                .build();
    }

    @Value
    @Builder
    private static class MockAuthorDetailsView implements AuthorDetailsView {

        String authorId;
        Long channelCount;
        Long videoCount;
        Long totalCommentCount;
        Long replyCount;
    }
}
