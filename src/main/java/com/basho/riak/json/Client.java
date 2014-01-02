package com.basho.riak.json;

import java.util.HashMap;
import java.util.Map;

import com.basho.riak.json.transports.http.HttpTransport;

/**
 * can custom cap, quorum controls (r, pr, w, pw) be used in a query?
 * anyone mind if i add a pingRJ method to the RJ api?
 * 
 * @author Randy Secrist
 */
public class Client {
  
  // TODO: consider making transport a proxy delegate
  private Transport transport;
  private Map<String, Collection> collection_cache;
  
  public Client(String host, int port) {
    super();
    init_internals(host, port);
  }
  
  public Collection createCollection(String collection_name) {
    return new Collection();
  }
  
  public String version() {
    return Version.getVersion();
  }
  
  public boolean ping() {
    return transport.ping();
  }
  
  public boolean pingKV() {
    return transport.pingKV();
  }
  
  public boolean pingRJ() {
    return transport.pingRJ();
  }
  
  public String toString() {
    return transport.toString();
  }
  
  // ---- Internals
  
  private void init_internals(String host, int port) {
    this.collection_cache = new HashMap<String, Collection>();
    this.transport = new HttpTransport(host, port);
  }
}
