package ca.metricalsky.winston.convert.api

import ca.metricalsky.winston.api.model.PullVideosOperation
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class PullVideosOperationToFetchOperationEntityConverter: Converter<PullVideosOperation, FetchOperationEntity> {

    override fun convert(source: PullVideosOperation) = FetchOperationEntity(
        operationType = FetchOperationEntity.Type.VIDEOS,
        objectId = source.channelHandle,
        mode = source.range?.name,
    )
}
