package com.basho.riak.json;

/**
 * This transport inteface defines the The minimum set of
 * functionality needed by the client api to communicate
 * with Riak JSON.
 * 
 * Functions are specific to the transport concern, but may
 * support inner level concerns such as serialization and
 * encryption.
 * 
 * @author Randy Secrist
 */
public interface Transport {

  /**
   * Return true if Riak's ping end point is contacted.
   * @return True if alive, false otherwise.
   */
  boolean ping();

  /**
   * Tests if riak_kv is up, deeper than ping.
   * @return True if riak_kv is up, false otherwise.
   */
  boolean pingKV();

  /**
   * Tests if riak_json is up, deeper than ping.
   * @return True if riak_json is up, false otherwise.
   */
  boolean pingRJ();

  /**
   * Fetch the schema given the collection name.
   * @param collection_name The name of the collection.
   * @return The schema which belongs to this collection.
   */
  Schema getSchema(String collection_name);

  /**
   * Returns true if the operation to associate a schema to
   * a collection is sent to Riak.  Actual association time
   * may vary.
   * 
   * @param collection_name The name of the collection.
   * @param schema The schema to associate.
   * @return True if the operation is a success, false otherwise.
   */
  boolean setSchema(String collection_name, Schema schema);

  /**
   * Returns true if the delete schema operation is successfully sent
   * to Riak.  Actual deletion time may vary.
   * 
   * @param collection_name The name of the collection who owns the schema.
   * @return True if the operation is a success, false otherwise.
   */
  boolean deleteSchema(String collection_name);

  /**
   * Insert a document into Riak.
   * @return The key of the document.
   */
  String insertDocument(String collection_name, Document document);

  /**
   * Removes a document from Riak.
   * @param collection_name The collection to remove it from.
   * @param document The document to remove.
   * @return True if the operation is a success, false otherwise.
   */
  boolean removeDocument(String collection_name, Document document);

  /**
   * Locates a single document by key.
   * @param collection_name The collection the document is within.
   * @param key The key of the document to locate.
   * @param type The expected concrete class type of the Document.
   * @return The document (as a java object) if found, null otherwise.
   */
  <T extends Document> T findByKey(String collection_name, String key, Class<T> type);

  /**
   * Returns the first document matched by the query.  Null otherwise.
   * @param collection_name The name of the collection to query.
   * @param query The query to invoke.
   * @return The first matching document the database can locate, if any.
   */
  <T extends Document> T findOne(String collection_name, Query<T> query);

  /**
   * Returns a QueryResult which contains multiple documents.  Null otherwise.
   * @param collection_name The name of the collection to query.
   * @param query The query to invoke.
   * @return A QueryResult which contains many documents, if any.
   */
  <T extends Document> QueryResult<T> findAll(String collection_name, Query<T> query);
}
