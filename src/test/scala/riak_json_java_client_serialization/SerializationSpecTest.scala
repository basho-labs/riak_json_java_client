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
    
    it ("[toJsonString] writes supported JsonSerializable objects as a String") {
      assertResult("[" + 
        "{\"name\":\"first_name\",\"type\":\"string\",\"required\":false}," +
        "{\"name\":\"last_name\",\"type\":\"string\",\"required\":true}," +
        "{\"name\":\"background\",\"type\":\"text\",\"required\":false}" + 
      "]") {
        serializer.toJsonString(schema)
      }
      val validTypes =
        Table(
          ("t"),
          (schema),
          (field)
        )
      forAll (validTypes) { (t: JsonSerializable) =>
        whenever (t != null) {
          val result = serializer.toJsonString(t)
          result.length() should be > 0
        }
      }
    }
    
    it ("[toJsonString] should explode when using unsupported types") {
      class Person (firstname:String,lastname:String) extends JsonSerializable {
        var fname = firstname; var lname = lastname;
      }
      val invalidTypes =
        Table(
          ("t"),
          (new Person("Walter", "White"))
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