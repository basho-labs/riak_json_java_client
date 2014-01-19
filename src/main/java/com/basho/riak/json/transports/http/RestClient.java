package com.basho.riak.json.transports.http;

import java.io.InputStream;
import java.net.URI;

/**
 *
 * @author Randy Secrist
 */
public interface RestClient {
  Response sendGetOrDelete(URI uri, Method method);
  Response sendPostOrPut(URI uri, Method method, InputStream input);
}
