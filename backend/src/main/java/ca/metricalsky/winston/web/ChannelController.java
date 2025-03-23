package ca.metricalsky.winston.web;

import ca.metricalsky.winston.dto.ChannelDto;
import ca.metricalsky.winston.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @GetMapping
    public List<ChannelDto> list() {
        return channelService.findAll();
    }

    @GetMapping("/{channelId}")
    public ChannelDto findById(@PathVariable String channelId) {
        return channelService.findById(channelId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested channel was not found."));
    }
}
