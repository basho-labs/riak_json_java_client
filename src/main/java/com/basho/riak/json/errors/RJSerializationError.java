package com.basho.riak.json.errors;

/**
 * Thrown to indicate a serialization / deserialization
 * failure has occurred.
 * 
 * @author Randy Secrist
 */
public class RJSerializationError extends RuntimeException {
  private static final long serialVersionUID = -7263435796281903647L;
  
  private byte[] data;

  public RJSerializationError(String message) {
    super(message);
  }
  
  public RJSerializationError(String message, Throwable cause) {
    super(message, cause);
  }
  
  public RJSerializationError(byte[] data, String message, Throwable cause) {
    super(message, cause);
    this.data = data;
  }
  
  public byte[] getData() {
    return data;
  }
}
