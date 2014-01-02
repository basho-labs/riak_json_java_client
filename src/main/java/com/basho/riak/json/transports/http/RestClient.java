package com.basho.riak.json.transports.http;

import java.net.URI;

import org.apache.http.StatusLine;

/**
 *
 * @author Randy Secrist
 */
public interface RestClient {
  StatusLine sendRequest(URI uri, Method method, byte[] data);
}
