package ca.metricalsky.winston.database.entity

import jakarta.persistence.Basic
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "thumbnails")
class ThumbnailEntity(

    @Id
    @Column(name = "id")
    var id: String,

    @Basic(optional = false)
    @Column(name = "url")
    var url: String,

    @Basic(optional = false)
    @Column(name = "image")
    var image: ByteArray
)
