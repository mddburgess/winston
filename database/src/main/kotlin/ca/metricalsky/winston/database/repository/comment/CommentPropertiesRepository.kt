package ca.metricalsky.winston.database.repository.comment

import ca.metricalsky.winston.database.entity.comment.CommentPropertiesEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentPropertiesRepository: JpaRepository<CommentPropertiesEntity, String>
