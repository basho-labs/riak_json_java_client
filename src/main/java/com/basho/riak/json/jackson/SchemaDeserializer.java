package com.basho.riak.json.jackson;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.basho.riak.json.Field;
import com.basho.riak.json.Schema;
import com.basho.riak.json.Schema.Builder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class SchemaDeserializer extends JsonDeserializer<Schema> {

  @Override
  public Schema deserialize(JsonParser parser, DeserializationContext context)
      throws IOException, JsonProcessingException {
        
    ObjectCodec oc = parser.getCodec();
    
    Builder b = new Schema.Builder();
    for (Iterator<List<Field>> map = oc.readValues(parser, new TypeReference<List<Field>>() {}); map.hasNext();)
      b.addFields(map.next());
    return b.build();
  }

}
