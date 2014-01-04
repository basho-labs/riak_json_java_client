package com.basho.riak.json.transports.http;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.StatusLine;

/**
 *
 * @author Randy Secrist
 */
public interface RestClient {
  StatusLine sendGetOrDelete(URI uri, Method method);
  StatusLine sendPostOrPut(URI uri, Method method, InputStream input);
}
