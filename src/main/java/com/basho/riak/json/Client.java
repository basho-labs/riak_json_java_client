package com.basho.riak.json;

import com.basho.riak.json.transports.http.HttpTransport;

/**
 * A client for the Riak JSON API.  The client uses a transport
 * which abstracts the actual protocol used to communicate with
 * the RJ API.
 * 
 * @author Randy Secrist
 */
public class Client {
  
  private Transport transport;
  
  public Client(String host, int port) {
    super();
    init_internals(host, port);
  }
  
  /**
   * Creates a new reference to a new or existing
   * collection.
   * 
   * @param name The name of the collection to create.
   * @return A collection reference.
   */
  public Collection createCollection(String name) {
    return new Collection(name, transport);
  }
  
  /**
   * Returns the version of the client library.
   * @return The version of the client library.
   */
  public String version() {
    return Version.getVersion();
  }
  
  /**
   * Returns true if the riak ping api returns 200.
   * False otherwise.
   * 
   * @return Boolean based on /ping == 200.
   */
  public boolean ping() {
    return transport.ping();
  }
  
  /**
   * A more detailed check than ping to check if riak_kv
   * is responding normally.
   * 
   * @return True if riak_kv responds normally, false otherwise.
   */
  public boolean pingKV() {
    return transport.pingKV();
  }
  
  /**
   * Returns true if riak_json is responding normally
   * false otherwise.
   * 
   * @return True if riak_json responds normally, false otherwise.
   */
  public boolean pingRJ() {
    return transport.pingRJ();
  }
  
  public String toString() {
    return transport.toString();
  }
  
  // ---- Internals
  
  private void init_internals(String host, int port) {
    this.transport = new HttpTransport(host, port);
  }
}
