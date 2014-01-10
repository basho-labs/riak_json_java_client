package riak_json_java_client

import scala.beans.BeanProperty

import org.scalatest.BeforeAndAfter
import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._

import com.basho.riak.json._
import com.basho.riak.json.Field.Type._
import com.basho.riak.json.jackson._
import com.fasterxml.jackson.annotation.JsonIgnore;

class DocumentSpecTest extends FunSpec with MockitoSugar with Matchers {
  
  class MySquare (length:Int, width:Int) extends Document {
    // use @JsonIgnore as well if omitted key from
    // serialized json is desired
    @BeanProperty var key: String = _
    
    private var _length = length
    private var _width = width

    def getLength = _length
    def getWidth = _width
    
    /* don't serialize this tuple */
    @JsonIgnore def getSize = Tuple2(_length, _width)
  }

  describe ("document specification tests") {
    val serializer = new DefaultSerializer()
    val document = new MySquare(640, 480)

    it ("should not have a key") {
      assert(document.getKey == null)
            
      val json = serializer.toJsonString(document)
      json.length() should be > 0
    }

  }
  
}
