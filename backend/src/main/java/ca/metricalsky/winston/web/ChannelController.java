package ca.metricalsky.winston.web;

import ca.metricalsky.winston.api.ChannelsApi;
import ca.metricalsky.winston.api.model.Channel;
import ca.metricalsky.winston.api.model.ChannelProperties;
import ca.metricalsky.winston.api.model.ListChannelsResponse;
import ca.metricalsky.winston.api.model.PatchOperation;
import ca.metricalsky.winston.dao.ChannelDataService;
import ca.metricalsky.winston.exception.AppException;
import ca.metricalsky.winston.exception.ErrorCode;
import ca.metricalsky.winston.service.ChannelService;
import ca.metricalsky.winston.utils.JsonPatchUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChannelController implements ChannelsApi {

    private final ChannelDataService channelDataService;
    private final ChannelService channelService;
    private final JsonPatchUtils jsonPatchUtils;

    @Override
    public ResponseEntity<ListChannelsResponse> listChannels(Boolean includeArchived) {
        var channels = channelService.getAllChannels(BooleanUtils.isTrue(includeArchived));
        var response = new ListChannelsResponse()
                .channels(channels);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Channel> getChannel(String handle) {
        var channel = channelService.getChannelByHandle(handle);

        return ResponseEntity.ok(channel);
    }

    @Override
    public ResponseEntity<ChannelProperties> patchChannelProperties(
            String handle,
            List<PatchOperation> patchOperations
    ) {
        var channel = channelDataService.findChannelByHandle(handle)
                .orElseThrow(() -> new AppException(ErrorCode.CHANNEL_NOT_FOUND));

        var channelProperties = channel.getProperties();
        var patchedChannelProperties = jsonPatchUtils.applyPatch(channelProperties, patchOperations);
        channel.setProperties(patchedChannelProperties);

        channelDataService.saveChannelProperties(channel);

        return ResponseEntity.ok(patchedChannelProperties);
    }
}
