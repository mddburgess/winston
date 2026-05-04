package ca.metricalsky.winston.database.test.faker.providers.comment

import ca.metricalsky.winston.database.entity.comment.CommentEntity
import ca.metricalsky.winston.database.entity.video.VideoEntity
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import net.datafaker.providers.base.AbstractProvider
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

class CommentEntityProvider(faker: DatabaseFaker): AbstractProvider<DatabaseFaker>(faker) {

    fun id() = faker.regexify("Ug[A-Za-z0-9_-]{24}")

    fun minimalEntity(video: VideoEntity? = null) = CommentEntity(
        id = id(),
        videoId = video?.id,
    )

    fun completeEntity(video: VideoEntity? = null) = CommentEntity(
        id = id(),
        videoId = video?.id,
        textDisplay = faker.massEffect().quote(),
        textOriginal = faker.massEffect().quote(),
        likeCount = faker.random().nextLong(0L, 100L),
        totalReplyCount = faker.random().nextLong(0L, 100L),
        publishedAt = faker.timeAndDate().past(7, TimeUnit.DAYS).atOffset(ZoneOffset.UTC),
        updatedAt = faker.timeAndDate().past(7, TimeUnit.DAYS).atOffset(ZoneOffset.UTC),
    )
}
