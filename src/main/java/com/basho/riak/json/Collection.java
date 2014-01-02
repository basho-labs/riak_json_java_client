package com.basho.riak.json;

/**
 *
 * @author Randy Secrist
 */
public class Collection {
  
  private String name;
  
  public Collection(String name) {
    super();
    this.name = name;
  }
  
  public boolean deleteRawJson(String key) { return false; }
  
  public boolean deleteSchema(String key) { return false; }
  
  public QueryResult find(Query query) { return null; }
  
  public Document findByKey(String key) { return null; }
  
  public Document findOne(Query query) { return null; }
  
  public String getRawJson(String key) { return null; }
  
  public Schema getSchema() { return null; }
  
  public boolean hasSchema() { return false; }
  
  /**
   * Inserts a new document into the collection.
   * @param document
   * @return The key of the inserted document.
   */
  public String insert(Document document) { return null; }
  
  /**
   * Inserts a JSON document into the collection using the specified Riak key.
   * @param key The riak_kv key assigned to the document.
   * @param json The riak_kv value as JSON.
   * @return The confirmed key of the inserted document.
   */
  public String insertRawJson(String key, String json) { return null; }
  
  public boolean remove(Document document) { return false; }
  
  /**
   * Assigns a schema to the collection.
   * @param schema The schema to assign.
   */
  public void setSchema(Schema schema) { return; }
  
  public boolean update(Document document) { return false; }
  
  public boolean update_raw_json(String key, String json) { return false; }
  
}
