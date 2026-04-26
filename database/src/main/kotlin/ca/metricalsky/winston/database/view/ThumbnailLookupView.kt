package ca.metricalsky.winston.database.view

import ca.metricalsky.winston.database.entity.ThumbnailEntity

interface ThumbnailLookupView {

    fun getThumbnailUrl(): String

    fun getThumbnail(): ThumbnailEntity?
}
