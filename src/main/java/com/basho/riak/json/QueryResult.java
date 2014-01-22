package com.basho.riak.json;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Randy Secrist
 */
public class QueryResult<T extends Document> {
  private int num_pages, per_page, page, total;
  private Collection<T> documents;

  public QueryResult(Collection<T> documents, Map<String,Object> attrs) {
    this.num_pages = (Integer) attrs.get("num_pages");
    this.per_page = (Integer) attrs.get("per_page");
    this.page = (Integer) attrs.get("page");
    this.total = (Integer) attrs.get("total");
    this.documents = documents;
  }

  public int numPages() {
    return num_pages;
  }
  public int perPage() {
    return per_page;
  }
  public int getPage() {
    return page;
  }
  public int getTotal() {
    return total;
  }

  public Collection<T> getDocuments() {
    return documents;
  } 
}
