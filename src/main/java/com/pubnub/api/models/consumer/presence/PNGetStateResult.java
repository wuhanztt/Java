package com.pubnub.api.models.consumer.presence;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Builder
@Getter
@ToString
public class PNGetStateResult {

    private Map<String, JsonNode> stateByUUID;

}
