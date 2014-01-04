package com.basho.riak.json;

import java.io.InputStream;

/**
 *
 * @author Randy Secrist
 */
public interface Transport {
  boolean ping();
  boolean pingKV();
  boolean pingRJ();
  
  Schema getSchema();
  boolean setSchema(Schema schema);
}
