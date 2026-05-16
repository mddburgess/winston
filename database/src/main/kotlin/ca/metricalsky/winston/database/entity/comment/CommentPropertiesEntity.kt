package ca.metricalsky.winston.database.entity.comment

import jakarta.persistence.Basic
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "comment_properties")
class CommentPropertiesEntity(

    @Id
    @Column(name = "comment_id")
    var commentId: String,

    @Basic(optional = false)
    @Column(name = "important")
    var important: Boolean,

    @Basic(optional = false)
    @Column(name = "hidden")
    var hidden: Boolean
)
