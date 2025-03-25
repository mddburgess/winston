package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.dto.fetch.FetchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FetchRequestHandlerFactory {

    private final FetchChannelService fetchChannelService;
    private final FetchVideosService fetchVideosService;

    public FetchRequestHandler getHandlerForRequest(FetchRequest fetchRequest) {
        if (fetchRequest.getChannel() != null) {
            return fetchChannelService;
        }
        if (fetchRequest.getVideos() != null) {
            return fetchVideosService;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
