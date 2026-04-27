package ca.metricalsky.winston.database.repository.channel

import ca.metricalsky.winston.database.entity.channel.ChannelPropertiesEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChannelPropertiesRepository: JpaRepository<ChannelPropertiesEntity, String>
