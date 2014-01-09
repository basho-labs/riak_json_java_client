package com.basho.riak.json.jackson;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SerializationTest {
  @Test
  public void canSerializeObjectWithProperties() {
    class Person implements JsonSerializable {
      String first_name;
      public Person(String first, String last) { first_name = first; }
      public String getFirst() { return first_name; }
    }
    Person p = new Person("Walter", "White");
    Serialization s = new DefaultSerializer();
    String json = s.toJsonString(p);
    assertNotNull(json);
    assertEquals("{\"first\":\"Walter\"}", json);
  }
}
