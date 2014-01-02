package com.basho.riak.json.transports.http;

/**
 *
 * @author Randy Secrist
 */
public enum Method {
  GET (1 << 0),
  PUT (1 << 1),
  DELETE (1 << 2);
  
  private final int code;
  
  Method(int method) {
    this.code = method;
  }
  public int code() { return code; }
  
  @Override
  public String toString() { return this.name().toLowerCase(); }
}