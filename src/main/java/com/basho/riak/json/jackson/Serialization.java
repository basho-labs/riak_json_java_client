package com.basho.riak.json.jackson;

import java.io.InputStream;
import java.io.OutputStream;

import com.basho.riak.json.Document;
import com.basho.riak.json.QueryResult;
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
  Schema fromSchemaJsonString(String schema_as_json);
  Schema fromSchemaInputStream(InputStream schema_stream);
  <T extends Document> T fromDocumentJsonString(String document_as_json, Class<T> type);
  <T extends Document> T fromDocumentInputStream(InputStream document_stream, Class<T> type);

  // read RJ Query Results
  <T extends Document> T fromRJResult(String json, Class<T> type);
  <T extends Document> QueryResult<T> fromRJResultSet(String json, Class<T> type);
}