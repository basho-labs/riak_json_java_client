package com.basho.riak.json.integ;

import java.util.concurrent.Callable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.basho.riak.json.Client;
import com.basho.riak.json.Collection;
import com.basho.riak.json.Field;
import com.basho.riak.json.Schema;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;
import static com.basho.riak.json.Field.Type.INTEGER;
import static com.basho.riak.json.Field.Type.STRING;
import static com.basho.riak.json.Field.Type.TEXT;
import static com.basho.riak.json.Field.Type.MULTI_STRING;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SchemaAdminITCase {
	
  private Collection collection;
	
  @Before
  public void before() {
    Client client = new Client("localhost", 10018);
    collection = client.createCollection("test_collection");
    collection.deleteSchema();
    await().atMost(5, SECONDS).until(collectionHasNoSchema());
  }
  
  @After
  public void after() {
    collection.deleteSchema();
    await().atMost(5, SECONDS).until(collectionHasNoSchema());
  }

  @Test
  public void collectionHasNoSchemaByDefault() {
    assertFalse(collection.hasSchema());
  }

  @Test
  public void collectionCanAddSchema() {
    Schema.Builder builder = new Schema.Builder();

    builder.addField(new Field("age_in_years", INTEGER).setRequired(true));
    builder.addField(new Field("first_name", STRING).setRequired(true));
    builder.addField(new Field("middle_name", STRING));
    builder.addField(new Field("last_name", STRING).setRequired(true));
    builder.addField(new Field("wall_o_text", TEXT));
    builder.addField(new Field("list_of_friends", MULTI_STRING).setRequired(false));
    builder.addField(new Field("level1", INTEGER));
    builder.addField(new Field("level1.level2", INTEGER));

    assertTrue(collection.setSchema(builder.build()));
    await().atMost(5, SECONDS).until(collectionHasSchema());
    assertTrue(collection.hasSchema());
  }

  private Callable<Boolean> collectionHasSchema() {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        return collection.hasSchema();
      }
    };
  }
  private Callable<Boolean> collectionHasNoSchema() {
    return new Callable<Boolean>() {
      public Boolean call() throws Exception {
        return !collection.hasSchema();
      }
    };
  }

}
