package ca.metricalsky.winston.convert.api

import ca.metricalsky.winston.api.model.PullChannelOperation
import ca.metricalsky.winston.api.model.PullCommentsOperation
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class PullCommentsOperationToFetchOperationEntityConverter: Converter<PullCommentsOperation, FetchOperationEntity> {

    override fun convert(source: PullCommentsOperation) = FetchOperationEntity(
        operationType = FetchOperationEntity.Type.COMMENTS,
        objectId = source.videoId,
    )
}
