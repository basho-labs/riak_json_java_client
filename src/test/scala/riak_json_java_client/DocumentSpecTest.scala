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
  
  class MyLine (length:Int) extends Document {
    @BeanProperty var key: String = _
    private var _length = length
    def getLength = _length    
  }

  describe ("document specification tests") {
    val serializer = new DefaultSerializer()
    val document = new MyLine(320)

    it ("may not have a key") {
      assert(document.getKey == null)
    }
  }
  
}
