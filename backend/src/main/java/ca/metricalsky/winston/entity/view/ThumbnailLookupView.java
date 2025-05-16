package ca.metricalsky.winston.entity.view;

import ca.metricalsky.winston.entity.ThumbnailEntity;

import java.util.Optional;

public interface ThumbnailLookupView {

    String getThumbnailUrl();

    Optional<ThumbnailEntity> getThumbnail();
}
