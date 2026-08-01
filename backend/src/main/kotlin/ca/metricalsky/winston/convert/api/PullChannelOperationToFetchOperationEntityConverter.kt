package ca.metricalsky.winston.convert.api

import ca.metricalsky.winston.api.model.PullChannelOperation
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class PullChannelOperationToFetchOperationEntityConverter: Converter<PullChannelOperation, FetchOperationEntity> {

    override fun convert(source: PullChannelOperation) = FetchOperationEntity(
        operationType = FetchOperationEntity.Type.CHANNELS,
        objectId = source.channelHandle,
    )
}
