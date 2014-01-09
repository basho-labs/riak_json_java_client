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
    
    it ("[toJsonString] writes JsonSerializable objects (with attributes) as a String") {
      assertResult("[" + 
        "{\"name\":\"first_name\",\"type\":\"string\",\"required\":false}," +
        "{\"name\":\"last_name\",\"type\":\"string\",\"required\":true}," +
        "{\"name\":\"background\",\"type\":\"text\",\"required\":false}" + 
      "]") {
        serializer.toJsonString(schema)
      }
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
    
    it ("[toOutputStream] writes a Schema to an OutputStream")_
    it ("[fromJsonString] builds a Schema from a String")_
    it ("[fromInputStream] builds a Schema from a InputStream")_
    
  }

}