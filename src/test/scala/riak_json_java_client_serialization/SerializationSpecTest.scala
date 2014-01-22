package riak_json_java_client_serialization

import scala.beans.BeanProperty

import org.scalatest.BeforeAndAfter
import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.scalatest.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.prop.PropertyChecks
import org.mockito.Mockito._
import org.mockito.Matchers._

import com.basho.riak.json._
import com.basho.riak.json.errors._
import com.basho.riak.json.Field.Type._
import com.basho.riak.json.jackson._
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream

class MySquare (l:Int, w:Int) extends Document {
  // use @JsonIgnore if removing the key from the
  // serialized json output is desired
  def this() =  this(0, 0)
  @BeanProperty var key: String = _
  @BeanProperty var length: Int = l
  @BeanProperty var width: Int = w

  /* don't serialize this tuple */
  @JsonIgnore def getSize = Tuple2(length, width)
}

class SerializationSpecTest extends FunSpec with Matchers with PropertyChecks {
  class ValidPerson (first_name:String) extends JsonSerializable {
    private var _fname = first_name
    def getFirstName = _fname
  }

  class InvalidPerson (first_name:String, last_name:String) extends JsonSerializable {
    private var _fname = first_name; private var _lname = last_name
    def firstname = _fname; def lastname = _lname
  }

  describe ("serialization spec tests") {
    val serializer = new DefaultSerializer()
    val field = new Field("background", TEXT)
    val schema = new Schema.Builder()
      .addField(new Field("first_name", STRING))
      .addField(new Field("last_name", STRING).setRequired(true))
      .addField(field)
      .build()
    val document = new MySquare(1024, 768)

    it ("[toJsonString] returns null if passed null") {
      assert(serializer.toJsonString(null) == null)
    }

    it ("[toJsonString] can write a Schema to a String") {
      assertResult("[" + 
        "{\"name\":\"first_name\",\"type\":\"string\",\"required\":false}," +
        "{\"name\":\"last_name\",\"type\":\"string\",\"required\":true}," +
        "{\"name\":\"background\",\"type\":\"text\",\"required\":false}" + 
      "]") {
        serializer.toJsonString(schema)
      }
    }

    it ("[toJsonString] can write a Field to a String") {
      assertResult("{\"name\":\"last_name\",\"type\":\"string\",\"required\":true}") {
        serializer.toJsonString(schema.getFields().get(1));
      }
    }

    it ("[toJsonString] writes compatible JsonSerializable objects as a String") {
      val validTypes =
        Table(
          ("t"),
          (document),
          (schema),
          (field),
          (new ValidPerson("Walter"))
        )
      forAll (validTypes) { (t: JsonSerializable) =>
        whenever (t != null) {
          val result = serializer.toJsonString(t)
          result.length() should be > 0
        }
      }
    }

    it ("[toJsonString] should explode when using types w/o getters") {
      val invalidTypes =
        Table(
          ("t"),
          (new InvalidPerson("Walter", "White"))
        )    
      forAll (invalidTypes) { (t: JsonSerializable) =>
        evaluating {
          serializer.toJsonString(t)
        } should produce [RJSerializationError]
      }
    }

    it ("[toOutputStream] writes a Schema to an OutputStream") {
      val stream = new ByteArrayOutputStream()
      serializer.toOutputStream(schema, stream)
      stream.toByteArray().length should be > 0
    }

    it ("[fromSchemaJsonString] explodes if it gets a non json String") {
      val non_json = "|HL7|NON_JSON|Sillyness"
      intercept[RJSerializationError] {
        serializer.fromSchemaJsonString(non_json)
      }
      intercept[RJSerializationError] {
        val stream = new ByteArrayInputStream(non_json.getBytes())
        serializer.fromSchemaInputStream(stream)
      }
    }

    it ("[fromSchemaJsonString] explodes if it gets a unmappable json String") {
      val random_json = "{\"foo\":\"bar\"}"
      intercept[RJSerializationError] {
        serializer.fromSchemaJsonString(random_json);
      }
      intercept[RJSerializationError] {
        val stream = new ByteArrayInputStream(random_json.getBytes())
        serializer.fromSchemaInputStream(stream)
      }
    }

    it ("[fromSchemaJsonString] builds a Field from a String") {
      val field_json = "[{\"name\":\"baz\",\"type\":\"integer\",\"required\":true}]"
      val field = serializer.fromSchemaJsonString(field_json).getFields().get(0)
      field.getName() should be === "baz"
      field.getType() should be === INTEGER
      field.isRequired() should be === true
    }

    describe ("deserialize schema tests") {
      val schema_json = "[" + 
        "{\"name\":\"foo\",\"type\":\"string\",\"required\":true}," +
        "{\"name\":\"bar\",\"type\":\"text\",\"required\":false}]"

      it ("[fromSchemaJsonString] builds a Schema from a String") {
        val schema = serializer.fromSchemaJsonString(schema_json)
        val fields = schema.getFields()
        fields.size() should be === 2
      }

      it ("[fromSchemaInputStream] builds a Schema from a InputStream") {
        val stream = new ByteArrayInputStream(schema_json.getBytes())
        val schema = serializer.fromSchemaInputStream(stream)
        val fields = schema.getFields()
        fields.size() should be === 2
      }
    }

    describe ("document serialization tests") {
      val square = new MySquare(640,480)
      val square_json = "{\"length\":640,\"width\":480}"
      it ("[fromDocumentJsonString] builds a Document from a String") {
        val result = serializer.fromDocumentJsonString(square_json, square.getClass())
        result.getLength should be === 640
        result.getWidth should be === 480
      }

      it ("[fromDocumentInputStream] builds a Document from a InputStream") {
        val stream = new ByteArrayInputStream(square_json.getBytes())
        val result = serializer.fromDocumentInputStream(stream, square.getClass())
        result.getLength should be === 640
        result.getWidth should be === 480
      }

      it ("[toJsonString] can serialize a Document") {
        val json = serializer.toJsonString(square)
        assert(json != null)
      }

      it ("[fromDocumentJsonString] returns null if passed null") {
        val result = serializer.fromDocumentJsonString(null, square.getClass());
        result should be === null
      }

      it ("[fromDocumentJsonString] returns null if passed empty json array") {
        val result = serializer.fromDocumentJsonString("[]", square.getClass());
        result should be === null
      }

      it ("[fromDocumentJsonString] explodes if it gets a unmappable json String") {
        val random_json = "{\"foo\":\"bar\"}"
        intercept[RJSerializationError] {
          serializer.fromDocumentJsonString(random_json, square.getClass());
        }
        intercept[RJSerializationError] {
          val stream = new ByteArrayInputStream(random_json.getBytes())
          serializer.fromDocumentInputStream(stream, square.getClass())
        }
      }

      it ("[fromRJResult] should use _id attribute as key") {
        val rj_json = "{\"_id\":\"some_key\",\"length\":640,\"width\":480}"
        val result = serializer.fromRJResult(rj_json, square.getClass())
        result.getKey should be === "some_key"
        result.getLength should be === 640
        result.getWidth should be === 480
      }

      it ("[fromRJResult] explodes if it gets a unmappable json String") {
        val rj_json = "{\"_id\":\"some_key\",\"foo\":640,\"bar\":480}"
        intercept[RJSerializationError] {
          serializer.fromRJResult(rj_json, square.getClass());
        }
      }

      it ("[fromRJResult] returns null if passed null") {
        val result = serializer.fromRJResult(null, square.getClass());
        result should be === null
      }

      it ("[fromRJResult] returns null if passed empty json array") {
        val result = serializer.fromRJResult("[]", square.getClass());
        result should be === null
      }

      it ("[fromRJResultSet] should build QueryResult") {
        val rs_json = """
        |{
        |  "data": [
        |    {"_id":"low_rez","length":640,"width":480},
        |    {"_id":"higher_rez","length":1024,"width":768}
        |  ],
        |  "num_pages": 2,
        |  "per_page": 1000,
        |  "page": 1,
        |  "total": 2
        |}
        """.stripMargin

        val result = serializer.fromRJResultSet(rs_json, square.getClass())
        result.getDocuments().size() should be === 2
        result.numPages() should be === 2
        result.perPage() should be === 1000
        result.getPage() should be === 1
        result.getTotal() should be === 2
      }

    }
    
  }

}