package ca.metricalsky.winston.database.entity.channel

import jakarta.persistence.Basic
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "channel_properties")
class ChannelPropertiesEntity(

    @Id
    @Column(name = "channel_id")
    var channelId: String,

    @Basic(optional = false)
    @Column(name = "archived")
    var archived: Boolean
)
