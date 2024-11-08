package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class ChannelProfileService {

    private final YouTubeService youTubeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public ChannelProfileService(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    // Fetches both channel profile details and the latest videos
    public CompletionStage<JsonNode> fetchChannelProfile(String channelId) {
        CompletionStage<JsonNode> channelDetails = youTubeService.fetchChannelDetails(channelId);
        CompletionStage<JsonNode> channelVideos = youTubeService.fetchChannelVideos(channelId);

        // Combine channel details and video items into a single JSON response
        return channelDetails.thenCombine(channelVideos, (details, videos) -> {
            ObjectNode profileJson = objectMapper.createObjectNode();
            profileJson.set("channelInfo", details);
            profileJson.set("videos", videos);
            return profileJson;
        });
    }
}
