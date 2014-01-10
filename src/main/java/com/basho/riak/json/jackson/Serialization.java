package com.basho.riak.json.jackson;

import java.io.InputStream;
import java.io.OutputStream;

import com.basho.riak.json.Schema;

/**
 * Expresses the seralization concern of the Riak Json
 * client library.
 * 
 * Attempts to be as type-explicit as possible to reduce
 * the possibility of {@link RJSerializationErrors}.
 * 
 * @author Randy Secrist
 */
public interface Serialization {
  // writers
  String toJsonString(JsonSerializable object);
  void toOutputStream(JsonSerializable json, OutputStream stream);
    
  // readers
  Schema fromJsonString(String schema_as_json);
  Schema fromInputStream(InputStream schema_stream);
}