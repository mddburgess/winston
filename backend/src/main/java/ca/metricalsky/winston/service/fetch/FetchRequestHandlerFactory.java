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
    private final FetchCommentsService fetchCommentsService;

    public FetchRequestHandler getHandlerForRequest(FetchRequest fetchRequest) {
        if (fetchRequest.getChannel() != null) {
            return fetchChannelService;
        }
        if (fetchRequest.getVideos() != null) {
            return fetchVideosService;
        }
        if (fetchRequest.getComments() != null) {
            return fetchCommentsService;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
