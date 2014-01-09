package riak_json_java_client_serialization

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
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream

class SerializationSpecTest extends FunSpec with Matchers with PropertyChecks {

  describe ("serialization spec tests") {
    val serializer = new DefaultSerializer()
    val field = new Field("background", TEXT)
    val schema = new Schema.Builder()
      .addField(new Field("first_name", STRING))
      .addField(new Field("last_name", STRING).setRequired(true))
      .addField(field)
      .build()
      
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
      class ValidPerson (first_name:String) extends JsonSerializable {
        private var _fname = first_name
        def getFirstName = _fname
      }
      val validTypes =
        Table(
          ("t"),
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
      class InvalidPerson (first_name:String, last_name:String) extends JsonSerializable {
        private var _fname = first_name; private var _lname = last_name
        def firstname = _fname; def lastname = _lname
      }
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
    
    it ("[fromJsonString] explodes if it gets a non json String") {
      val non_json = "|HL7|NON_JSON|Sillyness"
      intercept[RJSerializationError] {
        serializer.fromJsonString(non_json)
      }
      intercept[RJSerializationError] {
        val stream = new ByteArrayInputStream(non_json.getBytes())
        serializer.fromInputStream(stream)
      }
    }
    
    it ("[fromJsonString] explodes if it gets a random json String") {
      val random_json = "{\"foo\":\"bar\"}"
      intercept[RJSerializationError] {
        serializer.fromJsonString(random_json);
      }
      intercept[RJSerializationError] {
        val stream = new ByteArrayInputStream(random_json.getBytes())
        serializer.fromInputStream(stream)
      }
    }

    it ("[fromJsonString] builds a Field from a String") {
      val field_json = "[{\"name\":\"baz\",\"type\":\"integer\",\"required\":true}]"
      val field = serializer.fromJsonString(field_json).getFields().get(0)
      field.getName() should be === "baz"
      field.getType() should be === INTEGER
      field.isRequired() should be === true
    }
    
    describe ("deserialize schema tests") {
      val schema_json = "[" + 
        "{\"name\":\"foo\",\"type\":\"string\",\"required\":true}," +
        "{\"name\":\"bar\",\"type\":\"text\",\"required\":false}]"

      it ("[fromJsonString] builds a Schema from a String") {
        val schema = serializer.fromJsonString(schema_json)
        val fields = schema.getFields()
        fields.size() should be === 2
      }
    
      it ("[fromInputStream] builds a Schema from a InputStream") {
        val stream = new ByteArrayInputStream(schema_json.getBytes())
        val schema = serializer.fromInputStream(stream)
        val fields = schema.getFields()
        fields.size() should be === 2
      }
    }
    
  }

}