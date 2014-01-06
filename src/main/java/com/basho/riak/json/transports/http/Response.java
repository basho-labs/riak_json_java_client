package com.basho.riak.json.transports.http;

public class Response {
  public int status;
  public byte[] body;

  public Response(int status, byte[] body) {
    this.status = status;
    this.body = body;
  }

  public int status() {
    return status;
  }
	
  public byte[] body() {
    return body;
  }
}
