package com.basho.riak.json;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Randy Secrist
 */
public class QueryResult<T> {
  private int num_pages, per_page, page, total;
  private List<T> documents;

  public QueryResult(List<T> documents, Map<String,Object> attrs) {
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

  public List<T> getDocuments() {
    return documents;
  } 
}
