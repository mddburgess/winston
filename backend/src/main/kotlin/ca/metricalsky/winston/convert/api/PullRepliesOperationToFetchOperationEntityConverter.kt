package ca.metricalsky.winston.convert.api

import ca.metricalsky.winston.api.model.PullRepliesOperation
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class PullRepliesOperationToFetchOperationEntityConverter: Converter<PullRepliesOperation, FetchOperationEntity> {

    override fun convert(source: PullRepliesOperation) = FetchOperationEntity(
        operationType = FetchOperationEntity.Type.REPLIES,
        objectId = when {
            source.commentId.isNullOrBlank() -> source.videoId.orEmpty()
            else -> source.commentId
        },
        mode = when {
            source.commentId.isNullOrBlank() -> "FOR_VIDEO"
            else -> "FOR_COMMENT"
        },
    )
}
