package ca.metricalsky.winston.convert.api

import ca.metricalsky.winston.api.model.PullRequest
import ca.metricalsky.winston.database.entity.fetch.FetchOperationEntity
import ca.metricalsky.winston.database.entity.fetch.FetchRequestEntity
import org.springframework.context.annotation.Lazy
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class PullRequestToFetchRequestEntityConverter(
    @Lazy private var conversionService: ConversionService
): Converter<PullRequest, FetchRequestEntity> {

    override fun convert(source: PullRequest) = FetchRequestEntity(
        operations = source.operations.map {
            conversionService.convert(it, FetchOperationEntity::class.java)
        }.toMutableList(),
    )
}
