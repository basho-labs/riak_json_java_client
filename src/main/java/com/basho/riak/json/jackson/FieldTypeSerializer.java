package com.basho.riak.json.jackson;

import java.io.IOException;

import com.basho.riak.json.Field.Type;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class FieldTypeSerializer extends JsonSerializer<Type> {

  @Override
  public void serialize(Type value, JsonGenerator generator, SerializerProvider provider)
      throws IOException, JsonProcessingException {
        
    generator.writeStartObject();
    generator.writeFieldName("type");
    generator.writeString(value.toString());
    generator.writeEndObject();
  }

}
