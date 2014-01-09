package com.basho.riak.json.errors;

/**
 * Thrown to indicate a nondescript failure has occurred within
 * the Riak Json Java client library.
 * 
 * @author Randy Secrist
 */
public class RJException extends RuntimeException {
  private static final long serialVersionUID = -469102633988449598L;

  public RJException(String message) {
    super(message);
  }

  public RJException(String message, Throwable cause) {
    super(message, cause);
  }
}
