package com.basho.riak.json.jackson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.basho.riak.json.Field;
import com.basho.riak.json.Schema;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implements the serialization concerns.
 * 
 * @author Randy Secrist
 */
public class DefaultSerializer implements Serialization {
  
  // http://wiki.fasterxml.com/JacksonFAQThreadSafety
  private static final ObjectMapper mapper = new ObjectMapper();
    
  public String toJsonString(JsonSerializable object) {
    String rtnval = null;
    try {
      rtnval = mapper.writeValueAsString(object);
    }
    catch (JsonProcessingException e) {
        // TODO: better error handling
        // throw a runtime exception in case the impossible happens
        throw new InternalError("Unexpected JsonProcessingException: " + e.getMessage());
    }
    return rtnval;
  }
  
  public void toOutputStream(Schema schema, OutputStream stream) {
    try {
      mapper.writeValue(stream, schema);
    }
    catch (JsonProcessingException e) {
        // TODO: handle schema serialization (write) failure modes
    }
      catch (IOException e) {
        // TODO: handle schema serialization (read) failure modes
      }
  }
  
  public Schema fromJsonString(String json) {
      Schema rtnval = null;
      try {
        rtnval = mapper.readValue(json, Schema.class);
      }
      catch (JsonMappingException | JsonParseException e) {
        // TODO: handle schema serialization (read) failure modes
      }
      catch (IOException e) {
        // TODO: handle schema serialization (read) failure modes
      }
      return rtnval;
  }
  
  public Schema fromInputStream(InputStream stream) {
      Schema rtnval = null;
      try {
        rtnval = mapper.readValue(stream, Schema.class);
      }
      catch (JsonMappingException | JsonParseException e) {
        // TODO: handle schema read failure modes
      }
      catch (IOException e) {
        // TODO: handle schema read failure modes
      }
      return rtnval;
  }

}
