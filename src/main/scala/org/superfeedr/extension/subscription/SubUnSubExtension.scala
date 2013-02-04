package org.superfeedr.extension.subscription

import java.net.URL
import java.util.ArrayList
import java.util.Iterator
import java.util.List
import scala.collection.JavaConversions._

object SubUnSubExtension {
  final val TYPE_SUBSCRIPTION: Boolean = true
  final val TYPE_UNSUBSCRIPTION: Boolean = false
}

class SubUnSubExtension(feedsURLs: List[URL], val jid: String, val subscription: Boolean) extends PseudoPubSubPacketExtension {
  if (feedsURLs == null || jid == null) {
    throw new IllegalArgumentException("need URL to subscribe/unsubscribe to")
  }

  val feedURLs = new ArrayList[SubUnSubFeedExtension](feedsURLs.size)

  for (url <- feedsURLs) {
    if (subscription) {
      this.feedURLs.add(new SubscriptionFeedExtension(jid, url))
    }
    else {
      this.feedURLs.add(new UnSubscriptionFeedExtension(jid, url))
    }
  }


  def getFeedURLs: Iterator[SubUnSubFeedExtension] = {
    if (feedURLs == null) null else feedURLs.iterator
  }

  def toXMLInternal: String = {
    val builder: StringBuilder = new StringBuilder
    import scala.collection.JavaConversions._
    for (feed <- feedURLs) {
      builder.append(feed.toXML)
    }
    builder.toString
  }
}