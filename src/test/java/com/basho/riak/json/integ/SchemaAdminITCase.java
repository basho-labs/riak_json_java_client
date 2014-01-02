package com.basho.riak.json.integ;

import org.junit.Test;

import com.basho.riak.json.Client;
import com.basho.riak.json.Collection;
import com.basho.riak.json.Schema;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SchemaAdminITCase {

  @Test
  public void collectionHasNoSchema() {
    Client client = new Client("localhost", 10018);
    Collection collection = client.createCollection("test_collection");
    assertFalse(collection.hasSchema());
  }

  @Test
  public void collectionCanAddSchema() {
    Client client = new Client("localhost", 10018);
    Collection collection = client.createCollection("test_collection");
    Schema schema = new Schema();
    /* TODO: define fields api
    schema.addStringField( ... );
    schema.addTextField( ... )
    schema.addIntegerField( ... )
    schema.addMultiStringField( ... )
    */
    collection.setSchema(schema);
    assertTrue(collection.hasSchema());   
  }
}
