package com.basho.riak.json;

import java.util.HashMap;
import java.util.Map;

import com.basho.riak.json.jackson.FieldTypeDeserializer;
import com.basho.riak.json.jackson.FieldTypeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Field {
  // Required Attributes
  private String name;
  private Type type;
  
  // Optional Attributes
  private boolean required;
  
  protected Field() {
    super();
  }
  
  public Field(String name, Type type) {
    super();
    this.name = name;
    this.type = type;
  }
  
  public String getName() { return name; }

  public Type getType() { return type; }
  
  public boolean isRequired() { return required; }
  
  public Field setRequired(boolean required) {
    this.required = required;
    return this;
  }

  @JsonSerialize(using = FieldTypeSerializer.class)
  @JsonDeserialize(using = FieldTypeDeserializer.class)
  public enum Type {
    INTEGER (1 << 0),
    STRING (1 << 1),
    TEXT (1 << 2),
    MULTI_STRING (1 << 3);
    
    private final int code;
    
    private static Map<Integer,Type> codeValueMap =
      new HashMap<>(4);
    
    static {
      for (Type type : Type.values())
        codeValueMap.put(type.code, type);
    }
    
    Type(int code) {
      this.code = code;
    }
    public int code() { return code; }
    
    public static Type value(int code) {
      return codeValueMap.get(code);
    }
    
    public String toString() {
      return this.name().toLowerCase();
    }  
  }

}
