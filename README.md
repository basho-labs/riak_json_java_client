# Riak JSON Java Client
[![Build Status](https://travis-ci.org/randysecrist/riak_json_java_client.png?branch=master)](https://travis-ci.org/randysecrist/riak_json_java_client)

Java Client Library for Riak JSON (https://github.com/basho-labs/riak_json)

## Installation
This library has been released as a snapshot and can be referenced using Maven.

```xml
<dependency>
  <groupId>com.basho.riak</groupId>
  <artifactId>riak-json</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>

<repositories>
  <repository>
    <id>Basho Labs's - Snapshots</id>
    <url>https://github.com/randysecrist/randysecrist-mvn-repo/raw/master/snapshots</url>
  </repository>
</repositories>
```

## Building from Source
A java 1.7 and scala 2.10.3 runtime (to run the unit tests) are needed to build from source.

```bash
git clone git@github.com:basho-labs/riak_json_java_client.git
cd riak_json_java_client
mvn install
```
## Unit Testing
To run both unit and integration tests:

```bash
mvn verify -P itest
```

Note: By default, integration tests assume that Riak is listening on ```127.0.0.1:10018```
(the result of ```make devrel```).

To run just the unit tests:
```
mvn test
```
## Usage
### Creating / Referencing a Collection
```java
import com.basho.riak.json.Client;

Client client = new Client("localhost", 8098);

// A new or existing collection
collection = client.createCollection("cities");
```

### Schema Administration
```java
// You may set an optional schema
// (or skip this step and go straight to inserting documents)
// Supported field types:
//   - string (no spaces, think of a url slug)
//   - text (spaces allowed)
//   - multi_string (an array of strings, no spaces)
//   - integer

Schema.Builder builder = new Schema.Builder();
builder.addField(new Field("city", TEXT).setRequired(true));
builder.addField(new Field("state", STRING).setRequired(true));
builder.addField(new Field("zip_codes", MULTI_STRING));
builder.addField(new Field("population", INTEGER));
builder.addField(new Field("country", STRING).setRequired(true));
Schema schema = builder.build();

// Store the schema
collection.setSchema(schema);

// Check to see if schema is present
collection.hasSchema() => true

// Read a stored schema for a collection
Schema schema_result = collection.getSchema();

// Delete the schema (and the index) for the collection
// WARNING: This deletes the index for the collection, so previously saved documents
//          will not show up in queries!
collection.deleteSchema();
```

### Define a Document Model
```java
// can use jackson annotations to control serialization
// such as @JsonIgnore
class MyDocument implements Document {
  public MyDocument() { super(); }
  private String key, city, state, country;
  public String getKey() { return this.key; }
  public void setKey(String key) { this.key = key; }
  public String getCity() { return this.city; }
  public void setCity(String city) { this.city = city; }
  public String getState() { return this.state; }
  public void setState(String state) { this.state = state; }
  public String getCountry() { return this.country; }
  public void setCountry(String country) { this.country = country; }
}
```

### Reading and Writing Documents
```java
// You can insert a document with no key
// RiakJson generates a UUID type key and returns it
MyDocument slc = new MyDocument();
slc.setCity("Salt Lake City");
slc.setState("UT");
slc.setCountry("USA");
String new_key = collection.insert(slc);

// New Key (# => e.g. 'EmuVX4kFHxxvlUVJj5TmPGgGPjP')
// is auto assigned to document:
assertEquals(new_key, slc.getKey());

// Populate the cities collection with data
MyDocument nyc = new MyDocument();
nyc.setKey("nyc");
nyc.setCity("New York City");
nyc.setState("NY");
nyc.setCountry("USA");
collection.insert(nyc);

MyDocument boston = new MyDocument();
boston.setKey("boston");
boston.setCity("Boston");
boston.setState("MA");
boston.setCountry("USA");
collection.insert(boston);

MyDocument sf = new MyDocument();
sf.setKey("sf");
sf.setCity("San Francisco");
sf.setState("CA");
sf.setCountry("USA");
collection.insert(sf);

// Read a document (load by key)
MyDocument result = collection.findByKey("nyc", MyDocument.class);
result.getCity();  // => "New York City"
```

### Querying RiakJson - findOne() and findAll()
See [RiakJson Query Docs](https://github.com/basho-labs/riak_json/blob/master/docs/query.md) 
for a complete list of valid query parameters.
```java
// Exact match on "city" field

String q = "{\"city\":\"San Francisco\"}";
Query<MyDocument> query = new Query<MyDocument>(q, MyDocument.class);
MyDocument document = collection.findOne(query);
document.getCity() // => "San Francisco"

// Find all documents that match the "country" field exactly
String q = "{\"country\":\"USA\"}";
Query<MyDocument> query = new Query<MyDocument>(q, MyDocument.class);
QueryResult<MyDocument> results = collection.findAll(query);
results.getDocuments().size();  // => 4
results.numPages(); // => 1 -- total pages in result set
results.getPage(); // => 0 -- current page (zero-indexed)
results.perPage(); // results per page, defaults to 100
```

#### Limiting Query Results
```java
// Find all US cities, limit results to 10 per page
String q = "{\"country\":\"USA\", \"$per_page\":10}";
Query<MyDocument> query = new Query<MyDocument>(q, MyDocument.class);
QueryResult<MyDocument> results = collection.findAll(query);
results.perPage(); // => 10
```

#### Page Offsets
```java
// Find all US cities, retrieve 2nd page (zero-offset)
String q = "{\"country\":\"USA\", \"$per_page\":10, \"$page\":1}";
Query<MyDocument> query = new Query<MyDocument>(q, MyDocument.class);
QueryResult<MyDocument> results = collection.findAll(query);
results.getPage(); // => 1
```

#### Next Steps
We need a query builder that eliminates the need to write queries
as json strings.
```java
// Need to make this easier to write using Java
String q = "{\"country\":\"USA\", \"$per_page\":10, \"$page\":1}";
```

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
