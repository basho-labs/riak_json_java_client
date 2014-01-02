package com.basho.riak.json.transports.http;

import static com.basho.riak.json.transports.http.Method.GET;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Randy Secrist
 */
public class ApacheTransport implements RestClient {
  
  public ApacheTransport() {
    super();
  }

  @Override
  public StatusLine sendRequest(URI uri, Method method, byte[] data) {
    if (method == null)
      method = GET;
    
    HttpClientBuilder http_builder = HttpClientBuilder.create();
    CloseableHttpClient httpclient = http_builder.build();

    HttpUriRequest uri_request = null;
    switch (method) {
      case GET:
        uri_request = new HttpGet(uri);
        break;
      case PUT:
        uri_request = new HttpPut(uri);
        break;
      case DELETE:
        uri_request = new HttpDelete(uri);
        break;
    }
    
    HttpResponse response = null;
    try {
      response = httpclient.execute(uri_request);
      EntityUtils.consume(response.getEntity());
    }
    catch (HttpHostConnectException e) {
      // TODO: better error handling when riak down
      e.printStackTrace();
    }
    catch (Throwable t) {
      // IOException, ClientProtocolException
      t.printStackTrace();
    }
    
    if (response != null)
      return response.getStatusLine();

    final ProtocolVersion pv = uri_request.getProtocolVersion();
    return new StatusLine() {
      public ProtocolVersion getProtocolVersion() { return pv; }
      public int getStatusCode() { return 404; }
      public String getReasonPhrase() { return "httpclient returned null / not found"; }
    };
  }
  
}
