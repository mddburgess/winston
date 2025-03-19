package ca.metricalsky.winston.entity.view;

import ca.metricalsky.winston.entity.Thumbnail;

import java.util.Optional;

public interface ThumbnailLookup {

    String getThumbnailUrl();

    Optional<Thumbnail> getThumbnail();
}
