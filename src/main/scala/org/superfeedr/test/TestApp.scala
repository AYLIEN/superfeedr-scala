package org.superfeedr.test

import java.net.URL
import xml.XML
import scala.collection.JavaConversions._
import org.jivesoftware.smack.provider.ProviderManager
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Packet
import org.superfeedr.extension.notification.SuperfeedrEventExtension
import org.superfeedr.{OnNotificationHandler, OnResponseHandler, Superfeedr}
import org.superfeedr.extension.subscription.{SubscriptionFeedExtension, SubscriptionListExtension}

object TestApp {
  def main(args: Array[String]) {
    XMPPConnection.DEBUG_ENABLED = true
    setProviders()   // no need for this if you're using the Jar

    val feedr = new Superfeedr("USERNAME", "PASSWORD", "superfeedr.com")

    val feeds = List("http://feeds.bbci.co.uk/news/rss.xml",
                      "http://rss.cnn.com/rss/edition.rss")

    subscribe(feeds.map(new URL(_)),feedr)

    feedr.addOnNotificationHandler(notifHandler)
  }

  /**
   * Sets providers manually, no need for this if you're using the Jar
   */
  def setProviders() {
    val providersXML = XML.load("src/META-INF/smack.providers")

    for (provider <- (providersXML \\ "iqProvider")) {
      ProviderManager.getInstance().addIQProvider((provider \\ "elementName" text), (provider \\ "namespace" text), Class.forName(provider \\ "className" text).newInstance())
    }

    for (provider <- (providersXML \\ "extensionProvider")) {
      ProviderManager.getInstance().addExtensionProvider((provider \\ "elementName" text), (provider \\ "namespace" text), Class.forName(provider \\ "className" text).newInstance())
    }

    println("Providers registered")
  }

  def unsubscribeAll(feedr:Superfeedr) {
    feedr.unsubscribeAllFeeds(new OnResponseHandler {
      def onError(infos: String) {}

      def onSuccess(response: Packet) {
        println("Unsubscribed from all feeds")
      }
    })
  }

  def getAllSubscriptions(feedr:Superfeedr) {
    feedr.getSubscriptionList(new OnResponseHandler() {
      def onSuccess(response:Packet) {
        response.getExtensions.head.asInstanceOf[SubscriptionListExtension]
          .getSubscriptions.toList.map(_.getNode).foreach(println(_))
      }

      def onError(info:String) {
        println("Error")
      }
    })
  }

  def subscribe(urls:List[URL],feedr:Superfeedr) {
    feedr.subscribe(urls, new OnResponseHandler() {
      def onSuccess(response:Packet) {
        println("Connection established. Listening...")
        val subs = response.getExtensions.toList.asInstanceOf[List[SubscriptionFeedExtension]]
        subs.foreach(x=>println("Subscribed to: "+x.feedURL))
      }

      def onError(infos:String) {
        println("Error establishing connection:")
        println(infos)
      }
    })
  }

  def unsubscribe(urls:List[URL],feedr:Superfeedr) {
    feedr.unsubscribe(urls, new OnResponseHandler() {
      def onSuccess(o:Packet) {
        println("Unsubscribed")
      }
      def onError(s:String) {
        println("Error unsubscribing")
      }
    })
  }

  def notifHandler = new OnNotificationHandler {
    def onNotification(event:SuperfeedrEventExtension) {
      if (event.getItems.getItemsCount == 0) {
        println("No items")
      } else {
        println("Feed has %s items:".format(event.getItems.getItemsCount))
        for (item <- event.getItems.getItems) {
          val entry = item.getEntry
          println("- title: \n %s \n - link: \n %s \n - desc: \n %s".format(entry.title,entry.links(0).href,entry.summary))
        }
      }
      println("\n")
    }
  }
}
