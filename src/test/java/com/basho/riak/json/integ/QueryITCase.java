package com.basho.riak.json.integ;

import java.util.List;
import java.util.concurrent.Callable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.basho.riak.json.Client;
import com.basho.riak.json.Collection;
import com.basho.riak.json.Document;
import com.basho.riak.json.Query;
import com.basho.riak.json.QueryResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

// Sample Document Class
class MyDocument implements Document {
  public MyDocument() { super(); }
  private String key, firstname;
  @JsonIgnore public String getKey() { return this.key; }
  public void setKey(String key) { this.key = key; }
  public String getFirstname() { return this.firstname; }
  public void setFirstname(String firstname) { this.firstname = firstname; }
}

public class QueryITCase {

  private MyDocument document;
  private Collection collection;
  private String key;

  @Before
  public void before() {
    Client client = new Client("localhost", 10018);
    collection = client.createCollection("test_collection");
    key = "123";
    document = new MyDocument();
    document.setKey(key);
    document.setFirstname("Walter");
    collection.insert(document);
    await().atMost(5, SECONDS).until(documentIsReadable());
  }

  @After
  public void after() {
    document.setKey(key);
    collection.remove(document);
  }

  @Test
  public void insertWithKey() {
    String resulting_key = collection.insert(document);
    assertEquals(key, resulting_key);
  }

  @Test
  public void insertWithNoKeyThenRemove() {
    document.setKey(null);
    String resulting_key = collection.insert(document);
    assertTrue(resulting_key != null);
    assertFalse(key.equals(resulting_key));
    assertNotNull(document.getKey());

    await().atMost(5, SECONDS).until(documentIsReadable());
    assertTrue(collection.remove(document));
  }

  @Test
  public void updateExisting() {
    assertTrue(collection.update(document));
  }

  @Test
  public void queryByKey() {
    // not found case
    MyDocument result = collection.findByKey("not_found_key", MyDocument.class);
    assertNull(result);
    
    // found case
    result = collection.findByKey(key, MyDocument.class);
    assertEquals(MyDocument.class, result.getClass());
    assertNull(result.getKey());  // explicitly ignored in test model
    assertEquals(document.getFirstname(), result.getFirstname());
  }

  @Test
  public void queryOne() {
    String[] names = {"Drew", "Dmnitry", "Casey"};
    List<Document> documents = loadDocs(names);loadDocs(names);

    Query q = null;
    QueryResult qr = null;

    unloadDocs(documents);
  }

  @Test
  public void queryAll() {
    String[] names = {"Drew", "Dmnitry", "Casey"};
    List<Document> documents = loadDocs(names);

    Query q = null;
    QueryResult qr = null;

    unloadDocs(documents);
  }

  private List<Document> loadDocs(String[] firstnames) {
    Builder<Document> list = ImmutableList.builder();
    for (String firstname : firstnames) {
      MyDocument document = new MyDocument();
      document.setFirstname(firstname);
      collection.insert(document);
      list.add(document);
    }
    return list.build();
  }
  
  private void unloadDocs(List<Document> documents) {
    for (Document doc : documents) {
      collection.remove(doc);
    }
  }

  private Callable<Boolean> documentIsReadable() {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        Document found = collection.findByKey(document.getKey(), document.getClass());
        return found != null;
      }
    };
  }

}
