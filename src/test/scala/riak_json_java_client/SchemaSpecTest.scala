package riak_json_java_client

import org.scalatest.BeforeAndAfter
import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._

import com.basho.riak.json._
import com.basho.riak.json.Field.Type._
  
class SchemaSpecTest extends FunSpec with MockitoSugar with Matchers {

  describe ("schema specification tests") {
    it ("should build a schema with fields") {
      val schema = new Schema.Builder()
        .addField(new Field("name", STRING).setRequired(true))
        .addField(new Field("locations", MULTI_STRING))
        .build()
      
      schema.getFields().get(0).getName() should be === "name"
      schema.getFields().get(0).getType() should be === STRING
      schema.getFields().get(0).isRequired() should be === true
      schema.getFields().get(1).getName() should be === "locations"
      schema.getFields().get(1).getType() should be === MULTI_STRING
      schema.getFields().get(1).isRequired() should be === false
    }
  }
  
}
