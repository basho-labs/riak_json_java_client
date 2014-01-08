package com.basho.riak.json.transports.http;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;

import com.squareup.okhttp.OkHttpClient;

public class OKRestClient extends AbstractRestClient {

  @Override
  public Response sendGetOrDelete(URI uri, Method method) {
    OkHttpClient client = new OkHttpClient();

    HttpURLConnection connection = null;
    try {
      connection = client.open(uri.toURL());
      connection.setRequestMethod(method.toString().toUpperCase());
    }
    catch (MalformedURLException | ProtocolException e) {
      // TODO:  isolate failure modes; improve
      e.printStackTrace();
    }
    
    return getResult(connection);
  }

  @Override
  public Response sendPostOrPut(URI uri, Method method, InputStream input) {
    OkHttpClient client = new OkHttpClient();
    
    HttpURLConnection connection = null;
    try {
      connection = client.open(uri.toURL());
      connection.setRequestMethod(method.toString().toUpperCase());
      connection.setRequestProperty("content-type", "application/json");
    }
    catch (MalformedURLException | ProtocolException e) {
      // TODO:  isolate failure modes; improve
      e.printStackTrace();
    }
    
    try {
      OutputStream out = connection.getOutputStream();
      copyInputStream(input, out);
      out.close();
    }
    catch (IOException e) {
      // TODO:  isolate failure modes; improve
      e.printStackTrace();
    }
        
    return getResult(connection);
  }
    
  private Response getResult(HttpURLConnection connection) {
    int status = 0;
    byte[] body = null;
    try (InputStream in = connection.getInputStream()) {
      status = connection.getResponseCode();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      copyInputStream(in, out);
      body = out.toByteArray();
    }
    catch (ConnectException | FileNotFoundException e) {
      // TODO:  passing actual response codes seem impossible
      // 204 'might' not be the right one in some circumstances
      return new Response(204, null);
    }
    catch (IOException e) {
      // TODO:  isolate failure modes; improve
      e.printStackTrace();
    }
    return new Response(status, body);
  }
}
