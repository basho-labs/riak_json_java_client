package com.basho.riak.json.integ;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.basho.riak.json.Client;
import com.basho.riak.json.Collection;
import com.basho.riak.json.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;

import static org.junit.Assert.assertEquals;

public class QueryITCase {

  class MyDocument implements Document {
    private String key, firstname;
    @JsonIgnore public String getKey() { return this.key; }
    public void setKey(String key) { this.key = key; }
    public String getFirstname() { return this.firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
  }

  private Collection collection;

  @Before
  public void before() {
    Client client = new Client("localhost", 10018);
    collection = client.createCollection("test_collection");
  }

  @After
  public void after() {
  }

  @Test
  public void insertWithKey() {
    MyDocument doc = new MyDocument();
    doc.setKey("123");
    doc.setFirstname("Walter");
    String resulting_key = collection.insert(doc);
    assertEquals("123", resulting_key);
  }

  @Test
  public void insertWithNoKey() {
    MyDocument doc = new MyDocument();
    doc.setFirstname("Walter");
    String resulting_key = collection.insert(doc);
    assert(resulting_key != null);
  }

  @Test
  public void updateExisting() {
    // updates an existing document
  }

  @Test
  public void queryByKey() {
    // reads an existing document (loads it by key)
  }

  @Test
  public void queryOne() {
  }

  @Test
  public void queryAll() {
  }
}
