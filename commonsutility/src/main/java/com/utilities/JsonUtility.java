/*
 * @author
 *  Nidhi Chourasia created on 2018.
 */

package com.utilities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class JsonUtility {

    private ObjectMapper objectMapper;
    private ObjectMapper cutomObjectMapper;

    public JsonUtility() {
        this(new ObjectMapper()
                        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL),
                new ObjectMapper()
                        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL));

    }

    public JsonUtility(ObjectMapper objectMapper, ObjectMapper cusObjectMapper) {
        this.objectMapper = objectMapper;
        this.cutomObjectMapper = cusObjectMapper;
    }

    public <T> String convertToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonConversionException(e.getMessage());
        }
    }

    public List<String> convertToJson(List<Object> objectList) {
        return objectList.stream().map(object -> {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                throw new JsonConversionException(e.getMessage());
            }
        }).collect(toList());
    }

    public <T> T convertFromJson(String json, Class<T> type) throws IOException {
        return objectMapper.readValue(json, type);
    }

    public <T> String customConvertToJaon(T object, Class<T> type, String[] ignoreProperties) {

        try {
            cutomObjectMapper.addMixIn(type, MixIn.class);
            SimpleFilterProvider customFilters = new SimpleFilterProvider().addFilter("customFilters", SimpleBeanPropertyFilter.filterOutAllExcept(ignoreProperties));
            return objectMapper.setFilterProvider(customFilters).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            new JsonConversionException(e.getMessage());
            return null;
        }
    }

    public class JsonConversionException extends RuntimeException {
        public JsonConversionException(String message) {
            super(message);
        }
    }

    protected class MixIn {
    }
}
