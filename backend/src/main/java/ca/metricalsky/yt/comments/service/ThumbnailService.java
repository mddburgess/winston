package ca.metricalsky.yt.comments.service;

import ca.metricalsky.yt.comments.entity.Thumbnail;
import ca.metricalsky.yt.comments.entity.view.ThumbnailLookup;
import ca.metricalsky.yt.comments.repository.ThumbnailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

@Slf4j
@Service
public class ThumbnailService {

    private static final String THUMBNAIL_IMAGE_FORMAT = "jpg";

    private final ThumbnailRepository thumbnailRepository;

    @Autowired
    public ThumbnailService(ThumbnailRepository thumbnailRepository) {
        this.thumbnailRepository = thumbnailRepository;
    }

    public byte[] getChannelThumbnail(String channelId) {
        var lookup = thumbnailRepository.lookupByChannelId(channelId);
        return getOrFetchThumbnail(channelId, lookup);
    }

    public byte[] getVideoThumbnail(String videoId) {
        var lookup = thumbnailRepository.lookupByVideoId(videoId);
        return getOrFetchThumbnail(videoId, lookup);
    }

    private byte[] getOrFetchThumbnail(String id, ThumbnailLookup lookup) {
        if (lookup.getThumbnail().isEmpty()) {
            log.info("No cached thumbnail found for ID '{}', fetching", id);
            return fetchAndSaveThumbnail(id, lookup.getThumbnailUrl())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }

        var cachedThumbnail = lookup.getThumbnail().get();
        if (!cachedThumbnail.getUrl().equals(lookup.getThumbnailUrl())) {
            log.info("Cached thumbnail for ID '{}' is out of date, re-fetching", id);
            return fetchAndSaveThumbnail(id, lookup.getThumbnailUrl())
                    .orElse(cachedThumbnail.getImage());
        }

        log.info("Found cached thumbnail for ID '{}'", id);
        return cachedThumbnail.getImage();
    }

    private Optional<byte[]> fetchAndSaveThumbnail(String id, String thumbnailUrl) {
        var fetchedThumbnail = fetchThumbnailFromUrl(id, thumbnailUrl);
        fetchedThumbnail.ifPresent(thumbnail -> saveThumbnail(id, thumbnailUrl, thumbnail));
        return fetchedThumbnail;
    }

    private Optional<byte[]> fetchThumbnailFromUrl(String id, String thumbnailUrl) {
        log.info("Fetching thumbnail for ID '{}' from {}", id, thumbnailUrl);

        URL url;
        try {
            url = URI.create(thumbnailUrl).toURL();
        } catch (MalformedURLException ex) {
            log.error("Thumbnail URL for ID '{}' is not valid: {}", id, thumbnailUrl, ex);
            return Optional.empty();
        }

        BufferedImage image;
        try {
            image = ImageIO.read(url);
        } catch (IOException ex) {
            log.error("Failed to read thumbnail for ID '{}' from {}", id, thumbnailUrl, ex);
            return Optional.empty();
        }

        var outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, THUMBNAIL_IMAGE_FORMAT, outputStream);
        } catch (IOException ex) {
            log.error("Failed to write thumbnail for ID '{}' to byte array", id, ex);
            return Optional.empty();
        }

        return Optional.of(outputStream.toByteArray());
    }

    private void saveThumbnail(String id, String thumbnailUrl, byte[] image) {
        var thumbnail = new Thumbnail();
        thumbnail.setId(id);
        thumbnail.setUrl(thumbnailUrl);
        thumbnail.setImage(image);
        thumbnailRepository.save(thumbnail);
    }
}
