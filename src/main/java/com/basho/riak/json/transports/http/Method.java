package com.basho.riak.json.transports.http;

/**
 *
 * @author Randy Secrist
 */
public enum Method {
  HEAD (1 << 0),
  GET (1 << 1),
  POST (1 << 2),
  PUT (1 << 3),
  DELETE (1 << 4);
  
  private final int code;
  
  Method(int method) {
    this.code = method;
  }
  public int code() { return code; }
  
  @Override
  public String toString() { return this.name().toLowerCase(); }
}