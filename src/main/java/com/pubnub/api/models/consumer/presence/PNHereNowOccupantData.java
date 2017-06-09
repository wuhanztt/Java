package com.pubnub.api.models.consumer.presence;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PNHereNowOccupantData {
    private String uuid;
    private JsonNode state;
}
