package com.basho.riak.json.transports.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import com.basho.riak.json.Document;
import com.basho.riak.json.Query;
import com.basho.riak.json.QueryResult;
import com.basho.riak.json.Schema;
import com.basho.riak.json.Transport;
import com.basho.riak.json.errors.RJTransportError;
import com.basho.riak.json.jackson.DefaultSerializer;
import com.basho.riak.json.jackson.JsonSerializable;
import com.basho.riak.json.jackson.Serialization;

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
    throw new RJTransportError("not implemented yet");
  }
  
  public Schema getSchema(String collection_name) {
    URI uri = this.buildURL(getBaseCollectionURL(), "/" + collection_name + "/schema");
    
    String json = this.getJSON(uri);
    return (json != null) ? serializer.fromSchemaJsonString(json) : null;
  }
  
  public boolean setSchema(String collection_name, Schema schema) {
    URI uri = this.buildURL(getBaseCollectionURL(), "/" + collection_name + "/schema");
    InputStream in = this.fillInputPipe(schema);
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
      throw new RJTransportError("Invalid Riak URI", e);
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
  
  private InputStream fillInputPipe(JsonSerializable json) {
    PipedInputStream in = new PipedInputStream(4096);
    
    try (PipedOutputStream stream = new PipedOutputStream(in)) {
      serializer.toOutputStream(json, stream);
    }
    catch (IOException e) {
      throw new RJTransportError("Unexpected IOException while setting up response pipe.", e);
    }
    return in;
  }

  public String insertDocument(String collection_name, Document document) {
    String key = document.getKey();
    InputStream in = this.fillInputPipe(document);
    URI uri = null;
    if (key != null) {
      uri = this.buildURL(getBaseCollectionURL(), "/" + collection_name + "/" + key);
      Response response = sendPostOrPut(uri, PUT, in);
      return (response.status() == 204) ? key : new String(response.body());
    }
    else {
      uri = this.buildURL(getBaseCollectionURL(), "/" + collection_name);
      Response response = sendPostOrPut(uri, POST, in);
      String location_header = response.headers().get("Location");
      int key_location = location_header.lastIndexOf('/');
      String new_key = location_header.substring(key_location + 1, location_header.length());
      document.setKey(new_key);
      return (response.status() == 201) ? new_key : new String(response.body());
    }
  }

  public boolean removeDocument(String collection_name, Document document) {
    String key = document.getKey();
    URI uri = this.buildURL(getBaseCollectionURL(), "/" + collection_name + "/" + key);
    return (sendGetOrDelete(uri, DELETE).status() == 204) ? true : false;
  }

  public <T extends Document> T findByKey(String collection_name, String key, Class<T> type) {
    URI uri = this.buildURL(getBaseCollectionURL(), "/" + collection_name + "/" + key);
    String json = this.getJSON(uri);
    return (json != null) ? serializer.fromDocumentJsonString(json, type) : null;
  }

  /*
   * https://github.com/basho-labs/riak_json_ruby_client/blob/master/lib/riak_json/client.rb#L75-L79
   */
  public <T extends Document> T findOne(Query<T> query) {
    throw new RuntimeException("Not Implemented");
  }
  public <T extends Document> QueryResult<T> findAll(Query<T> query) {
    throw new RuntimeException("Not Implemented");
  }

  private String getJSON(URI uri) {
    Response response = sendGetRequest(uri);
    if (response.status() != 200)
      return null;

    return new String(response.body());
  }
}
