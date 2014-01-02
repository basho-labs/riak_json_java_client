package com.basho.riak.json.transports.http;

/**
 *
 * @author Randy Secrist
 */
public enum Protocol {
  HTTP (1 << 0),
  HTTPS (1 << 1);
  
  private final int protocol;
  
  Protocol(int protocol) {
    this.protocol = protocol;
  }
  public int protocol() { return protocol; }
  
  @Override
  public String toString() { return this.name().toLowerCase(); }
}