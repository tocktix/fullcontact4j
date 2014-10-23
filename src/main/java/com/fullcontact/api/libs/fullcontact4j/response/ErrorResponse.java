package com.fullcontact.api.libs.fullcontact4j.response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcontact.api.libs.fullcontact4j.FullContactException;

import java.io.IOException;

/**
 * When FullContact's APIs return a non-200 result, they also send in the response body
 * simple but useful information about the error.
 */
public class ErrorResponse {

    public int status;

    public String message;

    public String requestId;



    /**
     * Factory method to create a error response from json.
     * @param json
     * @return a new ErrorResponse
     * @throws com.fullcontact.api.libs.fullcontact4j.FullContactException if there is a parsing/mapping error.
     */
    public static ErrorResponse fromJson(String json) throws FullContactException {
        ObjectMapper mapper = new ObjectMapper();
        //Properties not present in the POJO are ignored instead of throwing exceptions
        //TODO put back in in release
        //mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //An empty string ("") is interpreted as null
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        try {
            return mapper.readValue(json, ErrorResponse.class);
        } catch(JsonMappingException e) {
            throw new FullContactException("Failed to convert json to an error response", e);
        } catch(JsonParseException e) {
            throw new FullContactException("Json is not valid format", e);
        } catch(IOException e) {
            throw new FullContactException("Unexpected exception when parsing json", e);
        }
    }
}