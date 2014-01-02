package com.basho.riak.json.transports.http;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.StatusLine;
import org.apache.http.client.utils.URIBuilder;

import com.basho.riak.json.Transport;
import com.basho.riak.json.errors.RiakJsonError;

import static com.basho.riak.json.transports.http.Method.GET;
import static com.basho.riak.json.transports.http.Method.PUT;
import static com.basho.riak.json.transports.http.Method.DELETE;
import static com.basho.riak.json.transports.http.Protocol.HTTP;

/**
 * An HTTP Thing
 * @author Randy Secrist
 */
public class HttpTransport implements Transport {

  private Protocol protocol;
  private String host;
  private int port;
  private RestClient client;
      
  public HttpTransport(String host, int port) {
    super();
    this.protocol = HTTP;
    this.host = host;
    this.port = port;
    setRestClient(new ApacheTransport());
  }
  
  public void setProtocol(Protocol protocol) {
    this.protocol = protocol;
  }
  
  public void setRestClient(RestClient client) {
    this.client = client;
  }
  
  public String getBaseRiakURL() {
    return protocol + "://" + host + ":" + port;
  }
  
  public String getBaseRiakJsonURL() {
    return getBaseRiakURL() + "/document";
  }
  
  public String getBaseCollectionURL() {
    return getBaseRiakJsonURL() + "/collection";
  }
  
  public String toString() {
    return getBaseRiakURL();
  }

  /* Transport API (Move to Interface) */
  // TODO: build schema api
  // TODO: build document api
  // TODO: build query api
  // use the base urls to do the following:
  //   setSchema, getSchema
  //   setDocument, getDocument
  //   Query
  
  public boolean ping() {
    URI uri = this.buildURL(getBaseRiakURL(), "/ping");
    return (sendRequest(uri).getStatusCode() == 200) ? true : false;
  }
  
  public boolean pingKV() {
    URI uri = this.buildURL(getBaseRiakURL(), "/buckets/not_a_bucket/keys/not_a_key");
    return (sendRequest(uri).getStatusCode() == 404) ? true : false;
  }
  
  public boolean pingRJ() {
    return false;
  }
  
  /* Deeply Internal (Used only by Transport API) */
  private URI buildURL(String base, String path) {
    try {
      URIBuilder uri_builder = new URIBuilder(base);
      uri_builder.setPath(path);
      return uri_builder.build();
    }
    catch (URISyntaxException e) {
      // TODO: better error handling
      throw new RiakJsonError("Invalid Configuration", e);
    }
  }
  
  private StatusLine sendRequest(URI uri) {
    return this.sendRequest(uri, null, null);
  }

  private StatusLine sendRequest(URI uri, Method method) {
    return this.sendRequest(uri, method, null);
  }
    
  private StatusLine sendRequest(URI uri, Method method, byte[] data) {
    return client.sendRequest(uri, method, data);
  }   
}
