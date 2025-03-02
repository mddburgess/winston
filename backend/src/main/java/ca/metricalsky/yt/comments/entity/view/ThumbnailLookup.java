package ca.metricalsky.yt.comments.entity.view;

import ca.metricalsky.yt.comments.entity.Thumbnail;

import java.util.Optional;

public interface ThumbnailLookup {

    String getThumbnailUrl();

    Optional<Thumbnail> getThumbnail();
}
