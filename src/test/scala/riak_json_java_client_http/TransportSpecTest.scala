package riak_json_java_client_http

import java.net.URI

import org.scalatest.BeforeAndAfter
import org.scalatest.FunSpec
import org.scalatest.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.prop.PropertyChecks
import org.mockito.Mockito._
import org.mockito.Matchers._

import com.basho.riak.json._
import com.basho.riak.json.errors._
import com.basho.riak.json.transports.http._

/**
 *
 * @author Randy Secrist
 */
class TransportSpecTest extends FunSpec with MockitoSugar with BeforeAndAfter with MustMatchers with PropertyChecks {
  before {}
  after {}

  describe ("http transport spec tests") {
    val transport = new HttpTransport("somehost", 9876);
    val response = mock[Response]
    val rest_client = mock[RestClient]

    it ("[ping] returns true if response code == 200") {
      when(response.status()).thenReturn(200)
      when(rest_client.sendGetOrDelete(any(classOf[URI]), any())).thenReturn(response)

      transport.setRestClient(rest_client)
      assert(transport.ping())
    }

    it ("[pingKV] returns true if response code == 404") {
      when(response.status()).thenReturn(404)
      when(rest_client.sendGetOrDelete(any(classOf[URI]), any())).thenReturn(response)

      transport.setRestClient(rest_client)
      assert(transport.pingKV())
    }
    
    it ("[pingRJ] throws an exception until implemented") {
      intercept[RJTransportError] {
        transport.pingRJ()
      }
    }

    it ("[setSchema] returns true if response code == 204") {
      when(response.status()).thenReturn(204)
      when(rest_client.sendPostOrPut(any(classOf[URI]), any(), any())).thenReturn(response)
      
      val schema = new Schema.Builder().build();
      assert(transport.setSchema("some_collection_name", schema))
    }
  }
} 

class TransportConfigTest extends FunSpec with BeforeAndAfter {
  before {}
  after {}

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
