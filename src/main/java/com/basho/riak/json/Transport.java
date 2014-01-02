package com.basho.riak.json;

/**
 *
 * @author Randy Secrist
 */
public interface Transport {
  boolean ping();
  boolean pingKV();
  boolean pingRJ();
}
