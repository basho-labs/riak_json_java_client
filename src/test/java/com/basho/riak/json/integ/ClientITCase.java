package com.basho.riak.json.integ;

import org.junit.Test;

import com.basho.riak.json.Client;

import static org.junit.Assert.assertTrue;

public class ClientITCase {

  @Test
  public void connectToRiakJson() {
    Client client = new Client("localhost", 10018);
    assertTrue(client.ping());
  }

}