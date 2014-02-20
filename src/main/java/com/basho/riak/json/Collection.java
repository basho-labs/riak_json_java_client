package com.basho.riak.json;


/**
 * The uppermost (public API) for a collection of searchable
 * documents stored using Riak JSON.
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

  /**
   * Returns the first document matched by the query.  Null otherwise.
   * @param query The query to invoke.
   * @return The first matching document the database can locate, if any.
   */
  public <T extends Document> T findOne(Query<T> query) {
    return transport.findOne(name, query);
  }

  /**
   * Returns a QueryResult which contains multiple documents.  Null otherwise.
   * @param query The query to invoke.
   * @return A QueryResult which contains many documents, if any.
   */
  public <T extends Document> QueryResult<T> findAll(Query<T> query) {
    return transport.findAll(name, query);
  }

  /**
   * Removes the schema from this collection.
   * @return True if the delete operation is passed to RJ.
   */
  public boolean deleteSchema() {
    return transport.deleteSchema(name);
  }

  /**
   * Returns the schema for this collection.
   * @return The schema for this collection.
   */
  public Schema getSchema() {
    return transport.getSchema(name);
  }

  /**
   * Returns true if this collection has a schema.  False otherwise.
   * @return Returns true if collection has a schema, false otherwise.
   */
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

  /**
   * Removes a document from this collection.
   * @param document The document to remove.
   * @return True if the remove operation is passed to RJ.
   */
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
