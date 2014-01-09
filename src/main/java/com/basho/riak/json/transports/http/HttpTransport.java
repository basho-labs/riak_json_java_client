package com.basho.riak.json.transports.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import com.basho.riak.json.Schema;
import com.basho.riak.json.Transport;
import com.basho.riak.json.errors.RJException;
import com.basho.riak.json.jackson.DefaultSerializer;
import com.basho.riak.json.jackson.Serialization;

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
  private Serialization serializer;
      
  public HttpTransport(String host, int port) {
    super();
    this.protocol = HTTP;
    this.host = host;
    this.port = port;
    setRestClient(new ApacheRestClient());
    setSerializer(new DefaultSerializer());
  }
  
  public void setProtocol(Protocol protocol) {
    this.protocol = protocol;
  }
  
  public void setRestClient(RestClient client) {
    this.client = client;
  }
  
  public void setSerializer(Serialization serializer) {
    this.serializer = serializer;
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
  // TODO: build document api
  // TODO: build query api
  // use the base urls to do the following:
  //   setDocument, getDocument
  //   Query
  
  public boolean ping() {
    URI uri = this.buildURL(getBaseRiakURL(), "/ping");
    return (sendGetRequest(uri).status() == 200) ? true : false;
  }
  
  public boolean pingKV() {
    URI uri = this.buildURL(getBaseRiakURL(), "/buckets/not_a_bucket/keys/not_a_key");
    return (sendGetRequest(uri).status() == 404) ? true : false;
  }
  
  public boolean pingRJ() {
    return false;
  }
  
  public Schema getSchema(String collection_name) {
    URI uri = this.buildURL(getBaseCollectionURL(), "/" + collection_name + "/schema");

    // get json
    Response response = sendGetRequest(uri);
    if (response.status() != 200)
      return null;

    String json = new String(response.body());
        
    return serializer.fromJsonString(json);
  }
  
  public boolean setSchema(String collection_name, Schema schema) {
	URI uri = this.buildURL(getBaseCollectionURL(), "/" + collection_name + "/schema");
    PipedInputStream in = new PipedInputStream(4096);
  
    try (PipedOutputStream out = new PipedOutputStream(in)) {
      serializer.toOutputStream(schema, out);
    }
    catch (IOException e) {
      // TODO: better error handling
      // throw a runtime exception in case the impossible happens
      throw new InternalError("Unexpected IOException: " + e.getMessage());
    }
  
    return (sendPostOrPut(uri, PUT, in).status() == 204) ? true : false;
  }
  
  public boolean deleteSchema(String collection_name) {
    URI uri = this.buildURL(getBaseCollectionURL(), "/" + collection_name + "/schema");
    return (sendGetOrDelete(uri, DELETE).status() == 204) ? true : false;
  }
  
  /* Deeply Internal (Used only by Transport API) */
  private URI buildURL(String base, String path) {
    try {
      URI uri = new URI(base + path);
      return uri;
    }
    catch (URISyntaxException e) {
      // TODO: better error handling
      throw new RJException("Invalid Application Configuration", e);
    }
  }
  
  private Response sendGetRequest(URI uri) {
    return this.sendGetOrDelete(uri, GET);
  }

  private Response sendGetOrDelete(URI uri, Method method) {
    return client.sendGetOrDelete(uri, method);
  }
      
  private Response sendPostOrPut(URI uri, Method method, InputStream input) {
    return client.sendPostOrPut(uri, method, input);
  }
}
