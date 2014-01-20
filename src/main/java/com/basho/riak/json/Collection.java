package com.basho.riak.json;


/**
 *
 * @author Randy Secrist
 */
public class Collection {

  private String name;
  private Transport transport;

  public Collection(String name, Transport transport) {
    super();
    this.name = name;
    this.transport = transport;
  }

  /**
   * Fetch document from Riak by key.
   * @param key The key of the document to locate.
   * @param type The expected concrete class type of the Document.
   * @return The document (as a java object) if found, null otherwise.
   */
  public <T extends Document> T findByKey(String key, Class<T> type) {
    return transport.findByKey(name, key, type);
  }

  public <T extends Document> T findOne(Query<T> query) {
    return transport.findOne(query);
  }
  
  public <T extends Document> QueryResult<T> find(Query<T> query) {
    return transport.findAll(query);
  }

  
  public boolean deleteSchema() {
    return transport.deleteSchema(name);
  }

  public Schema getSchema() {
    return transport.getSchema(name);
  }

  public boolean hasSchema() {
    return transport.getSchema(name) != null;
  }

  /**
   * Inserts a new document into the collection.
   * @param document
   * @return The key of the inserted document.
   */
  public String insert(Document document) {
    return transport.insertDocument(name, document);
  }

  public boolean update(Document document) {
    if (document == null || document.getKey() == null)
      return false;
    transport.insertDocument(name, document);
    return true;
  }

  public boolean remove(Document document) {
    if (document == null || document.getKey() == null)
      return false;
    return transport.removeDocument(name, document);
  }

  /**
   * Assigns a schema to the collection.
   * @param schema The schema to assign.
   */
  public boolean setSchema(Schema schema) {
    return transport.setSchema(name, schema);
  }

}
