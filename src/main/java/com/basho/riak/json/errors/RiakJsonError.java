package com.basho.riak.json.errors;

/**
 * Thrown by the client api in the event of a failure.
 * @author Randy Secrist
 */
public class RiakJsonError extends RuntimeException {
  private static final long serialVersionUID = -469102633988449598L;

  public RiakJsonError(String message) {
    super(message);
  }

  public RiakJsonError(String message, Throwable cause) {
    super(message, cause);
  }
}
