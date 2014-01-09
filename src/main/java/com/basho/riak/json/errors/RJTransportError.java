package com.basho.riak.json.errors;

/**
 * Thrown to indicate a problem communicating with a Riak node.
 * 
 * @author Randy Secrist
 */
public class RJTransportError extends RuntimeException {
  private static final long serialVersionUID = 1768279504975578102L;

  public RJTransportError(String message) {
    super(message);
  }
	  
  public RJTransportError(String message, Throwable cause) {
    super(message, cause);
  }
}
