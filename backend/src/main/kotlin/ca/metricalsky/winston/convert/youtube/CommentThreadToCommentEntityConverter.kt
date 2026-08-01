package ca.metricalsky.winston.convert.youtube

import ca.metricalsky.winston.database.entity.comment.CommentEntity
import com.google.api.services.youtube.model.CommentThread
import org.springframework.context.annotation.Lazy
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class CommentThreadToCommentEntityConverter(
    @Lazy private val conversionService: ConversionService,
) : Converter<CommentThread, CommentEntity> {

    override fun convert(source: CommentThread) = conversionService
        .convert(source.snippet?.topLevelComment, CommentEntity::class.java)
        ?.apply {
            totalReplyCount = source.snippet?.totalReplyCount
            replies = source.replies?.comments.orEmpty()
                .mapNotNull { conversionService.convert(it, CommentEntity::class.java) }
                .toMutableList()
        }
}
