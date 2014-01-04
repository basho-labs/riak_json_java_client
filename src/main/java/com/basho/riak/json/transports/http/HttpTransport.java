package com.basho.riak.json.transports.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.StatusLine;
import org.apache.http.client.utils.URIBuilder;

import com.basho.riak.json.Schema;
import com.basho.riak.json.Transport;
import com.basho.riak.json.errors.RiakJsonError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.basho.riak.json.transports.http.Method.HEAD;
import static com.basho.riak.json.transports.http.Method.GET;
import static com.basho.riak.json.transports.http.Method.POST;
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
  // TODO: build document api
  // TODO: build query api
  // use the base urls to do the following:
  //   setSchema, getSchema
  //   setDocument, getDocument
  //   Query
  
  public boolean ping() {
    URI uri = this.buildURL(getBaseRiakURL(), "/ping");
    return (sendGetRequest(uri).getStatusCode() == 200) ? true : false;
  }
  
  public boolean pingKV() {
    URI uri = this.buildURL(getBaseRiakURL(), "/buckets/not_a_bucket/keys/not_a_key");
    return (sendGetRequest(uri).getStatusCode() == 404) ? true : false;
  }
  
  public boolean pingRJ() {
    return false;
  }
  
  public Schema getSchema() {
    URI uri = this.buildURL(getBaseRiakURL(), "/buckets/test_java/keys/randy");

    // get json
    String json = null;
    
    // deserialize
    ObjectMapper mapper = new ObjectMapper();
    
    Schema rtnval = null;
    try {
      rtnval = mapper.readValue(json, Schema.class);
    }
    catch (JsonMappingException | JsonParseException e) {
      // TODO: handle schema read failure modes
    }
    catch (IOException e) {
      // TODO: handle schema read failure modes
    }
    return rtnval;
  }
  
  public boolean setSchema(Schema schema) {
    URI uri = this.buildURL(getBaseRiakURL(), "/buckets/test_java/keys/randy");
    PipedInputStream in = new PipedInputStream(4096);
  
    try (PipedOutputStream out = new PipedOutputStream(in)) {
      // serialize schema to json
      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(out, schema);
    }
    catch (Throwable t) {
      // TODO: better error handling
      t.printStackTrace();
    }
  
    return (sendPostOrPut(uri, POST, in).getStatusCode() == 204) ? true : false;
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
  
  private StatusLine sendGetRequest(URI uri) {
    return this.sendGetOrDelete(uri, GET);
  }

  private StatusLine sendGetOrDelete(URI uri, Method method) {
    return client.sendGetOrDelete(uri, method);
  }
      
  private StatusLine sendPostOrPut(URI uri, Method method, InputStream input) {
    return client.sendPostOrPut(uri, method, input);
  }
}
