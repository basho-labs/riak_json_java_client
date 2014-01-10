package com.basho.riak.json;

import com.basho.riak.json.jackson.JsonSerializable;

/**
 * 
 * @author Randy Secrist
 */
public interface Document extends JsonSerializable {

  /**
   * Returns the riak_kv key of the Document.
   * 
   * @return The key as a String.
   */
  String getKey();
  void setKey(String key);
}
