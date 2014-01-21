package com.basho.riak.json;

/**
 *
 * @author Randy Secrist
 */
public class Query<T extends Document> {
  private String query;
  private Class<T> type;
  
  public Query(String q, Class<T> type) {
    super();
    this.query = q;
    this.type = type;
  }
    
  public String getQuery() {
    return query;
  }
  
  public Class<T> getType() {
    return type;
  }
}
