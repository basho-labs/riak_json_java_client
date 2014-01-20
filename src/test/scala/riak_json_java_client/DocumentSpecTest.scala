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

class MyLine (len:Int) extends Document {
  def this() =  this(0)
  @BeanProperty var key: String = _
  private var _length = len
  def getLength: Int = _length
  def setLength(l: Int) = _length = l
}

class DocumentSpecTest extends FunSpec with MockitoSugar with Matchers {

  describe ("document specification tests") {
    val serializer = new DefaultSerializer()
    val document = new MyLine(320)

    it ("may not have a key") {
      assert(document.getKey == null)
    }

    it ("serializeable to json") {
      val json = serializer.toJsonString(document)
      assert(json != null)
      
      val result = serializer.fromDocumentJsonString(json, document.getClass())
      assert(result != null)
      result.getLength should be === document.getLength
    }
  }
  
}
