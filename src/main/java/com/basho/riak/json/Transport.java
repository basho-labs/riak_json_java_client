package com.basho.riak.json;

/**
 *
 * @author Randy Secrist
 */
public interface Transport {
  boolean ping();
  boolean pingKV();
  boolean pingRJ();
  
  Schema getSchema(String collection_name);
  boolean setSchema(String collection_name, Schema schema);
  boolean deleteSchema(String collection_name);
}
