package com.pubnub.api.models.server;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryForChannelsItem {

    @Getter private JsonNode message;

    @Getter private Long timetoken;

}
