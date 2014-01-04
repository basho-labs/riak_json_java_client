package com.basho.riak.json.transports.http;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import static com.basho.riak.json.transports.http.Method.GET;
import static com.basho.riak.json.transports.http.Method.DELETE;
import static com.basho.riak.json.transports.http.Method.POST;
import static com.basho.riak.json.transports.http.Method.PUT;

/**
 *
 * @author Randy Secrist
 */
public class ApacheTransport implements RestClient {
  
  public ApacheTransport() {
    super();
  }

  @Override
  public StatusLine sendGetOrDelete(URI uri, Method method) {
    if (method == null)
      method = GET;
    
    CloseableHttpClient httpclient = this.createClient();
    
    HttpUriRequest uri_request = null;
    if (method == GET)
      uri_request = new HttpGet(uri);
    else if (method == DELETE)
      uri_request = new HttpDelete(uri);
    
    HttpResponse response = this.makeRequest(httpclient, uri_request);
    
    if (response != null)
      return response.getStatusLine();
    
    return this.getResult(response, uri_request.getProtocolVersion());    
  }
  
  public StatusLine sendPostOrPut(URI uri, Method method, InputStream input) {
    CloseableHttpClient httpclient = this.createClient();
    
    HttpEntityEnclosingRequestBase uri_request = new HttpPost(uri);
    if (method == POST)
      uri_request = new HttpPost(uri);
    else if (method == PUT)
      uri_request = new HttpPut(uri);
    
    InputStreamEntity entity = new InputStreamEntity(input);
    entity.setContentType("application/json");
    uri_request.setEntity(entity);
    
    HttpResponse response = this.makeRequest(httpclient, uri_request);
    return this.getResult(response, uri_request.getProtocolVersion());    
  }
  
  private CloseableHttpClient createClient() {
    HttpClientBuilder http_builder = HttpClientBuilder.create();
    return http_builder.build();
  }
  
  private HttpResponse makeRequest(HttpClient client, HttpUriRequest uri_request) {
    HttpResponse response = null;
    try {
      response = client.execute(uri_request);
      EntityUtils.consume(response.getEntity());
    }
    catch (HttpHostConnectException e) {
      // TOOD: better exception handling for when riak is down
      e.printStackTrace();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
    return response;
  }
  
  private StatusLine getResult(HttpResponse response, final ProtocolVersion pv) {
    if (response != null)
      return response.getStatusLine();

    return new StatusLine() {
      public ProtocolVersion getProtocolVersion() { return pv; }
      public int getStatusCode() { return 404; }
      public String getReasonPhrase() { return "httpclient returned null / not found"; }
    };
  }
  
}
