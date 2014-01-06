package com.basho.riak.json.jackson;

import java.io.IOException;

import com.basho.riak.json.Schema;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SchemaSerializer extends JsonSerializer<Schema> {

  @Override
  public void serialize(Schema value, JsonGenerator generator, SerializerProvider provider)
      throws IOException, JsonProcessingException {
	 
    generator.writeObject(value.getFields());
  }

}
