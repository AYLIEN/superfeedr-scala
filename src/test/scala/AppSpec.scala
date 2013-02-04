import java.net.URL
import org.jivesoftware.smack.packet.Packet
import org.jivesoftware.smack.XMPPConnection
import org.specs2.mutable._
import org.superfeedr.extension.subscription.{SubscriptionListExtension, SubscriptionFeedExtension}
import org.superfeedr.test.TestApp
import org.superfeedr.{OnResponseHandler, Superfeedr}
import scala.collection.JavaConversions._

class AppSpec extends Specification {
  val DUMMY_FEED = new URL("http://feeds.bbci.co.uk/news/rss.xml")

  XMPPConnection.DEBUG_ENABLED = false
  TestApp.setProviders()
  val feedr = new Superfeedr("USERNAME", "PASSWORD", "superfeedr.com")

  var subs = List[URL]()
  var allSubs = List[URL]()
  var allSubsAfterUnsub = List(DUMMY_FEED)

  feedr.subscribe(List(DUMMY_FEED), new OnResponseHandler() {
    def onSuccess(response:Packet) {
      subs = response.getExtensions.toList.asInstanceOf[List[SubscriptionFeedExtension]].map(_.feedURL)

      feedr.getSubscriptionList(new OnResponseHandler() {
        def onSuccess(response:Packet) {
          if (response.getExtensions.head.isInstanceOf[SubscriptionListExtension]) {
            allSubs = response.getExtensions.head.asInstanceOf[SubscriptionListExtension].getSubscriptions.toList.map(_.getNode)

            feedr.unsubscribe(allSubs, new OnResponseHandler {
              def onError(infos: String) {throw new Exception("Error unsubscribing from all feeds")}

              def onSuccess(response: Packet) {
                feedr.getSubscriptionList(new OnResponseHandler {
                  def onError(infos: String) {throw new Exception("Error retrieving a list of all subscriptions")}

                  def onSuccess(response: Packet) {
                    if (response.getExtensions.head.isInstanceOf[SubscriptionListExtension]) {
                      allSubsAfterUnsub = response.getExtensions.head.asInstanceOf[SubscriptionListExtension].getSubscriptions.toList.map(_.getNode)
                    }
                  }
                })
              }
            })
          }
        }

        def onError(info:String) {throw new Exception("Error retrieving a list of all subscriptions")}
      })
    }

    def onError(infos:String) {
      throw new Exception("Error subscribing to dummy feed")
    }
  })

  "subscription results" should {
    "contain dummy feed in callback results" in {
      subs must eventually(3,500.milliseconds)(contain(DUMMY_FEED))
    }
    "contain dummy feed in getSubscriptionList result" in {
      allSubs must eventually(3,1000.milliseconds)(contain(DUMMY_FEED))
    }
    "be empty after unsubscription from all feeds" in {
      allSubsAfterUnsub must eventually(3,1500.milliseconds)(beEqualTo(List(new URL("http://superfeedr.com/dummy.xml"))))  // means empty as you can't unsubscribe from this feed
    }
  }
}
