package com.basho.riak.json.transports.http;

import java.util.Map;

public class Response {
  private int status;
  private Map<String,String> headers;
  private byte[] body;

  public Response(int status, byte[] body, Map<String,String> headers) {
    this.status = status;
    this.body = body;
    this.headers = headers;
  }

  public int status() {
    return status;
  }
	
  public byte[] body() {
    return body;
  }
  
  public Map<String,String> headers() {
    return headers;
  }
}
