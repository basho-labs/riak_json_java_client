package com.basho.riak.json.jackson;

import java.io.IOException;

import com.basho.riak.json.Field.Type;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class FieldTypeDeserializer extends JsonDeserializer<Type> {

  @Override
  public Type deserialize(JsonParser parser, DeserializationContext context)
      throws IOException, JsonProcessingException {
        
    ObjectCodec oc = parser.getCodec();
    JsonNode node = oc.readTree(parser);
    return Type.valueOf(node.get("type").textValue().toUpperCase());
  }

}
