package com.basho.riak.json.jackson;

import java.io.InputStream;
import java.io.OutputStream;

import com.basho.riak.json.Schema;

public interface Serialization {
  
  String toJsonString(JsonSerializable object);
  void toOutputStream(Schema schema, OutputStream stream);
  Schema fromJsonString(String schema_as_json);
  Schema fromInputStream(InputStream schema_stream);

}