package com.pubnub.api.managers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import lombok.Getter;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class MapperManager {

    @Getter
    private ObjectMapper objectMapper;
    @Getter
    private Converter.Factory converterFactory;

    public MapperManager() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.converterFactory = JacksonConverterFactory.create(this.getObjectMapper());
    }

    public boolean hasField(JsonNode element, String field) {
        return element.has(field);
    }

    public JsonNode getField(JsonNode element, String field) {
        return element.get(field);
    }

    public Iterator<JsonNode> getArrayIterator(JsonNode element) {
        return element.iterator();
    }

    public Iterator<JsonNode> getArrayIterator(JsonNode element, String field) {
        return element.get(field).iterator();
    }

    public Iterator<Map.Entry<String, JsonNode>> getObjectIterator(JsonNode element) {
        return element.fields();
    }

    public Iterator<Map.Entry<String, JsonNode>> getObjectIterator(JsonNode element, String field) {
        return element.get(field).fields();
    }

    public String elementToString(JsonNode element) {
        return element.asText();
    }

    public int elementToInt(JsonNode element, String field) {
        return element.get(field).asInt();
    }

    public String elementToString(JsonNode element, String field) {
        return element.get(field).asText();
    }

    public boolean isJsonObject(JsonNode element) {
        return element.isObject();
    }

    public ObjectNode getAsObject(JsonNode element) {
        return (ObjectNode) element;
    }

    public boolean getAsBoolean(JsonNode element, String field) {
        return element.get(field).asBoolean();
    }

    public void putOnObject(ObjectNode element, String key, JsonNode value) {
       element.set(key, value);
    }

    public JsonNode getArrayElement(JsonNode element, int index) {
        return ((ArrayNode) element).get(index);
    }

    public Long elementToLong(JsonNode element) {
        return element.asLong();
    }

    public Long elementToLong(JsonNode element, String field) {
        return element.get(field).asLong();
    }

    public ArrayNode getAsArray(JsonNode element) {
        return (ArrayNode) element;
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(String input, Class<T> clazz) throws PubNubException {
        try {
            return this.objectMapper.readValue(input, clazz);
        } catch (IOException e) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).errormsg(e.getMessage()).build();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(String input, TypeReference typeReference) throws PubNubException {
        try {
            return this.objectMapper.readValue(input, typeReference);
        } catch (IOException e) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).errormsg(e.getMessage()).build();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T convertValue(Object input, Class clazz) {
        return (T) this.objectMapper.convertValue(input, clazz);
    }

    public String toJson(Object input) throws PubNubException {
        try {
            return this.objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_JSON_ERROR).errormsg(e.getMessage()).build();
        }
    }

}
