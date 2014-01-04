package com.basho.riak.json.integ;

import org.junit.Test;

import com.basho.riak.json.Client;
import com.basho.riak.json.Collection;
import com.basho.riak.json.Field;
import com.basho.riak.json.Schema;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import static com.basho.riak.json.Field.Type.INTEGER;
import static com.basho.riak.json.Field.Type.STRING;
import static com.basho.riak.json.Field.Type.TEXT;
import static com.basho.riak.json.Field.Type.MULTI_STRING;

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
    Schema.Builder builder = new Schema.Builder("test_schema");

    builder.addField(new Field("age_in_years", INTEGER).setRequired(true));
    builder.addField(new Field("first_name", STRING).setRequired(true));
    builder.addField(new Field("middle_name", STRING));
    builder.addField(new Field("last_name", STRING).setRequired(true));
    builder.addField(new Field("wall_o_text", TEXT));
    builder.addField(new Field("list_of_friends", MULTI_STRING).setRequired(false));
    builder.addField(new Field("level1", INTEGER));
    builder.addField(new Field("level1.level2", INTEGER));

    collection.setSchema(builder.build());
    assertTrue(collection.hasSchema());
  }
}
