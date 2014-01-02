package riak_json_java_client_http

import java.net.URI

import org.scalatest.BeforeAndAfter
import org.scalatest.FunSpec
import org.scalatest.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._

import org.apache.http.HttpResponse
import org.apache.http.StatusLine

import com.basho.riak.json._
import com.basho.riak.json.transports.http._

/**
 *
 * @author Randy Secrist
 */
class TransportSpecTest extends FunSpec with MockitoSugar with BeforeAndAfter with MustMatchers {
  before {}
  after {}

  describe ("http transport spec tests") {
    val transport = new HttpTransport("somehost", 9876);
    val status_line = mock[StatusLine]
    val rest_client = mock[RestClient]

    it ("[ping] returns true if response code == 200") {
      when(status_line.getStatusCode()).thenReturn(200)
      when(rest_client.sendRequest(any(classOf[URI]), any(), any())).thenReturn(status_line)

      transport.setRestClient(rest_client)
      assert(transport.ping())
    }

    it ("[pingKV] returns true if response code == 404") {
      when(status_line.getStatusCode()).thenReturn(404)
      when(rest_client.sendRequest(any(classOf[URI]), any(), any())).thenReturn(status_line)

      transport.setRestClient(rest_client)
      assert(transport.pingKV())
    }

    it ("[somefun] does something") {
      val http_response = mock[HttpResponse]
    }
  }
} 

class TransportConfigTest extends FunSpec with BeforeAndAfter {
  before {}
  after {}

  describe ("http transport config") {
    val transport = new HttpTransport("somehost", 9876);
    describe("when new") {
      it ("should contain the host") {
        assert(transport.getBaseRiakURL().contains("somehost"))
      }
      it ("should contain the port") {
        assert(transport.getBaseRiakURL().contains("9876"))
      }
      it ("should have default protocol of http") {
        assert(transport.getBaseRiakURL().startsWith("http"))
      }
      it ("document url should end with document") {
        assert(transport.getBaseRiakJsonURL().startsWith("http"))
        assert(transport.getBaseRiakJsonURL().endsWith("document"))
      }
      it ("collection url should end with collection") {
        assert(transport.getBaseCollectionURL().startsWith("http"))
        assert(transport.getBaseCollectionURL().endsWith("collection"))
      }
    }
  }

}
