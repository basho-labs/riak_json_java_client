package com.basho.riak.json.integ;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.basho.riak.json.Client;
import com.basho.riak.json.Collection;
import com.basho.riak.json.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QueryITCase {

  class MyDocument implements Document {
    private String key, firstname;
    @JsonIgnore public String getKey() { return this.key; }
    public void setKey(String key) { this.key = key; }
    public String getFirstname() { return this.firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
  }

  private MyDocument document;
  private Collection collection;

  @Before
  public void before() {
    Client client = new Client("localhost", 10018);
    collection = client.createCollection("test_collection");
    document = new MyDocument();
  }

  @After
  public void after() {
  }

  @Test
  public void insertWithKey() {
	document.setKey("123");
	document.setFirstname("Walter");
    String resulting_key = collection.insert(document);
    assertEquals("123", resulting_key);
  }

  @Test
  public void insertWithNoKey() {
	document.setFirstname("Walter");
    String resulting_key = collection.insert(document);
    assertTrue(resulting_key != null);
  }

  @Test
  public void updateExisting() {
	document.setKey("123");
    document.setFirstname("White");
    assertTrue(collection.update(document));
  }

  @Test
  public void queryByKey() {
  }

  @Test
  public void queryOne() {
  }

  @Test
  public void queryAll() {
  }
}
