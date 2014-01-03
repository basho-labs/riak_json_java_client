package com.basho.riak.json;

import java.util.ArrayList;
import java.util.List;

public class Schema {

  private String schema_name;
  private List<Field> fields;

  public static class Builder {
    // Required Parameters
    private final String schema_name;
    
    // Optional Parameters
    private List<Field> fields;
    
    public Builder(String name) {
      this.schema_name = name;
      fields = new ArrayList<Field>();
    }
    
    public Builder addField(Field field) {
      fields.add(field);
      return this;
    }
    
    public Schema build() {
      return new Schema(this);
    }
  }
  
  private Schema(Builder builder) {
    super();
    schema_name = builder.schema_name;
    fields = builder.fields;
  }

  
}
