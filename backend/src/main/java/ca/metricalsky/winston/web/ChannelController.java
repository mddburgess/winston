package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.ChannelsApi;
import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.ListChannelsResponse;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChannelController implements ChannelsApi {

    private final ChannelDataService channelDataService;
    private final ChannelService channelService;

    @Override
    public ResponseEntity<ListChannelsResponse> listChannels() {
        var channels = channelService.getAllChannels();
        var response = new ListChannelsResponse()
                .channels(channels);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Channel> getChannel(String handle) {
        var channel = channelDataService.findChannelByHandle(handle)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "The requested channel was not found."));

        return ResponseEntity.ok(channel);
    }
}
