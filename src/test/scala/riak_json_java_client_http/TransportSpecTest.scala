package riak_json_java_client_http

import java.net.URI
import scala.beans.BeanProperty

import org.scalatest.BeforeAndAfter
import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._

import com.basho.riak.json._
import com.basho.riak.json.Field.Type._
import com.basho.riak.json.errors._
import com.basho.riak.json.transports.http._

/**
 *
 * @author Randy Secrist
 */
class TransportSpecTest extends FunSpec with MockitoSugar with Matchers with BeforeAndAfter {

  describe ("http transport spec tests") {
    val transport = new HttpTransport("somehost", 9876);
    val response = mock[Response]
    val rest_client = mock[RestClient]
    val collection_name = "some_collection_name"
    
    when(rest_client.sendGetOrDelete(any(classOf[URI]), any())).thenReturn(response)
    when(rest_client.sendPostOrPut(any(classOf[URI]), any(), any())).thenReturn(response)

    before {}
    after { reset(response) }
    
    it ("[ping] returns true if response code == 200") {
      when(response.status()).thenReturn(200)

      transport.setRestClient(rest_client)
      assert(transport.ping())
    }

    it ("[pingKV] returns true if response code == 404") {
      when(response.status()).thenReturn(404)
  
      transport.setRestClient(rest_client)
      assert(transport.pingKV())
    }
    
    it ("[pingRJ] throws an exception until implemented") {
      intercept[RJTransportError] {
        transport.pingRJ()
      }
    }
    
    describe ("transport schema tests") {
      val schema = new Schema.Builder().addField(new Field("first_name", STRING)).build();

      it ("[setSchema] returns true only if response code == 204") {
        when(response.status()).thenReturn(204)
        transport.setSchema(collection_name, schema) should be === true

        when(response.status()).thenReturn(404)
        transport.setSchema(collection_name, schema) should be === false
      }
    
      it ("[getSchema] returns a schema if response code == 200") {
        when(response.status()).thenReturn(200);
        when(response.body()).thenReturn(schema.toString().getBytes());
        
        val field = transport.getSchema(collection_name).getFields().get(0)
        field.getName() should be === "first_name"
        field.getType() should be === STRING
      }
    
      it ("[deleteSchema] returns true only if response code == 204") {
        when(response.status()).thenReturn(204)
        transport.deleteSchema(collection_name) should be === true

        when(response.status()).thenReturn(404)
        transport.deleteSchema(collection_name) should be === false
      }
    
    }
    
    describe ("transport document tests") {
      class MyLine (length:Int) extends Document {
        @BeanProperty var key: String = _
        private var _length = length
        def getLength = _length
      }
      val test_key = "my_key"
        
      it ("[insertDocument] returns the supplied key if response code == 204") {
        val document = new MyLine(19)
        document.setKey(test_key)

        when(response.status()).thenReturn(204);

        val result = transport.insertDocument(collection_name, document)
        result should be === test_key
      }

      it ("[insertDocument] returns the response body if response code != 204") {
        val expected_body = "some response body"
        val document = new MyLine(19)
        document.setKey(test_key)

        when(response.status()).thenReturn(200);
        when(response.body()).thenReturn(expected_body.getBytes());

        val result = transport.insertDocument(collection_name, document)
        result should be === expected_body
      }

    }

  }
} 

class TransportConfigTest extends FunSpec {

  describe ("http transport config") {
    val transport = new HttpTransport("somehost", 9876);
    describe("when new") {
      it ("[getBaseRiakURL] should contain the host") {
        assert(transport.getBaseRiakURL().contains("somehost"))
      }
      it ("[getBaseRiakURL] should contain the port") {
        assert(transport.getBaseRiakURL().contains("9876"))
      }
      it ("[getBaseRiakURL] should have default protocol of http") {
        assert(transport.getBaseRiakURL().startsWith("http"))
      }
      it ("[getBaseRiakJsonURL] should end with document") {
        assert(transport.getBaseRiakJsonURL().startsWith("http"))
        assert(transport.getBaseRiakJsonURL().endsWith("document"))
      }
      it ("[getBaseCollectionURL] should contain document and end with collection") {
        assert(transport.getBaseCollectionURL().startsWith("http"))
        assert(transport.getBaseCollectionURL().contains("document"))
        assert(transport.getBaseCollectionURL().endsWith("collection"))
      }
    }
  }

}
