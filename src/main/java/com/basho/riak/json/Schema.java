package com.basho.riak.json;

import java.util.ArrayList;
import java.util.List;

import com.basho.riak.json.jackson.SchemaDeserializer;
import com.basho.riak.json.jackson.SchemaSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SchemaSerializer.class)
@JsonDeserialize(using = SchemaDeserializer.class)
public class Schema {

  private List<Field> fields;

  public static class Builder {
    // Required Parameters
    
    // Optional Parameters
    private List<Field> fields;
    
    public Builder() {
      // add any req params
      fields = new ArrayList<>();
    }
    
    public Builder addField(Field field) {
      fields.add(field);
      return this;
    }
    
    public Builder addFields(List<Field> fields) {
      this.fields.addAll(fields);
      return this;
    }
    
    public Schema build() {
      return new Schema(this);
    }
  }
  
  protected Schema() {
	  super();
  }
  
  private Schema(Builder builder) {
    super();
    fields = builder.fields;
  }
    
  public List<Field> getFields() {
    return fields;
  }
  
  // TODO: add toString()
  
}
