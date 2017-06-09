package com.pubnub.api.models.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class SubscribeMessage {

    @JsonProperty("a")
    private String shard;

    @JsonProperty("b")
    private String subscriptionMatch;

    @JsonProperty("c")
    private String channel;

    @JsonProperty("d")
    private JsonNode payload;

    // TODO: figure me out
    //@SerializedName("ear")
    //private String payload;

    @JsonProperty("f")
    private String flags;

    @JsonProperty("i")
    private String issuingClientId;

    @JsonProperty("k")
    private String subscribeKey;

    //@SerializedName("s")
    //private String sequenceNumber;

    @JsonProperty("o")
    private OriginationMetaData originationMetadata;

    @JsonProperty("p")
    private PublishMetaData publishMetaData;

    //@SerializedName("r")
    //private Object replicationMap;

    @JsonProperty("u")
    private JsonNode userMetadata;

    //@SerializedName("w")
    //private String waypointList;
}
