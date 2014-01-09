package com.basho.riak.json.transports.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
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

import com.basho.riak.json.errors.RJTransportError;

import static com.basho.riak.json.transports.http.Method.GET;
import static com.basho.riak.json.transports.http.Method.DELETE;
import static com.basho.riak.json.transports.http.Method.POST;
import static com.basho.riak.json.transports.http.Method.PUT;
import static com.basho.riak.json.utils.StreamUtils.copyInputStream;

/**
 *
 * @author Randy Secrist
 */
public class ApacheRestClient extends AbstractRestClient {
  
  public ApacheRestClient() {
    super();
  }

  @Override
  public Response sendGetOrDelete(URI uri, Method method) {
    if (method == null)
      method = GET;
    
    CloseableHttpClient httpclient = this.createClient();
    
    HttpUriRequest uri_request = null;
    if (method == GET)
      uri_request = new HttpGet(uri);
    else if (method == DELETE)
      uri_request = new HttpDelete(uri);
    
    HttpResponse response = this.makeRequest(httpclient, uri_request);    
    return this.getResult(response, uri_request.getProtocolVersion());    
  }
  
  public Response sendPostOrPut(URI uri, Method method, InputStream input) {
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
    }
    catch (HttpHostConnectException e) {
      throw new RJTransportError("Failed Request", e);
    }
    catch (IOException e) {
      throw new RJTransportError("Failed Request", e);
    }
    return response;
  }
  
  private Response getResult(HttpResponse response, final ProtocolVersion pv) {
    if (response == null)
      return new Response(404, null);

    int status = response.getStatusLine().getStatusCode();
    HttpEntity entity = response.getEntity();
    
    if (entity == null) // usually b/c of a 204
    	return new Response(status, null);
    
    try (InputStream in = entity.getContent()) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      copyInputStream(in, out);
      return new Response(status, out.toByteArray());
    }
    catch (IOException e) {
      throw new RJTransportError("Failed Request", e);
    }
  }
  
}
