package com.basho.riak.json;

import java.util.ArrayList;
import java.util.List;

public class Schema {

  private String name;
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
  
  protected Schema() {
	  super();
  }
  
  private Schema(Builder builder) {
    super();
    name = builder.schema_name;
    fields = builder.fields;
  }
  
  public String getName() {
	  return name;
  }
  
  public List<Field> getFields() {
	  return fields;
  }
  
}
