package ca.metricalsky.winston.convert.youtube

import ca.metricalsky.winston.database.entity.author.AuthorEntity
import ca.metricalsky.winston.database.entity.comment.CommentEntity
import com.google.api.services.youtube.model.Comment
import org.apache.commons.lang3.StringUtils
import org.springframework.context.annotation.Lazy
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class CommentToCommentEntityConverter(
    @Lazy private val conversionService: ConversionService,
): Converter<Comment, CommentEntity> {

    override fun convert(source: Comment) = CommentEntity(
        id = source.id,
        videoId = source.snippet?.videoId,
        parentId = source.snippet?.parentId,
        author = AuthorEntity(
            id = source.snippet?.authorChannelId?.value,
            displayName = source.snippet?.authorDisplayName,
            channelUrl = source.snippet?.authorChannelUrl,
            profileImageUrl = source.snippet?.authorProfileImageUrl,
        ),
        textDisplay = source.snippet?.textDisplay,
        textOriginal = source.snippet?.textOriginal?.sanitized(),
        likeCount = source.snippet?.likeCount,
        publishedAt = conversionService.convert(source.snippet?.publishedAt, OffsetDateTime::class.java),
        updatedAt = conversionService.convert(source.snippet?.updatedAt, OffsetDateTime::class.java),
    )

    private fun String.sanitized() = StringUtils.remove(this, "\u0000")
}
