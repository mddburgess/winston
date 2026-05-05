package ca.metricalsky.winston.database.test.faker.providers.video

import ca.metricalsky.winston.database.entity.video.VideoCommentsEntity
import ca.metricalsky.winston.database.entity.video.VideoEntity
import ca.metricalsky.winston.database.test.faker.DatabaseFaker
import net.datafaker.providers.base.AbstractProvider

class VideoCommentsEntityProvider(faker: DatabaseFaker): AbstractProvider<DatabaseFaker>(faker) {

    fun completeEntity(video: VideoEntity) = VideoCommentsEntity(
        videoId = video.id!!,
        commentsDisabled = faker.bool().bool(),
        commentCount = faker.random().nextLong(0L, 100L),
        replyCount = faker.random().nextLong(0L, 100L),
        totalReplyCount = faker.random().nextLong(0L, 100L),
    )
}
