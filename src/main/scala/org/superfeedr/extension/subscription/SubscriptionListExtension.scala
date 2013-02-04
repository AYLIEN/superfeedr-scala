package org.superfeedr.extension.subscription

import java.util.Iterator
import java.util.List

class SubscriptionListExtension(var subscriptions: List[SubscriptionExtension] = null, var jid: String = null, var page: Long = 0L) extends PseudoPubSubPacketExtension {
  def toXMLInternal: String = {
    val builder: StringBuilder = new StringBuilder("<subscriptions ")
    if (jid != null) {
      builder.append("jid=\"")
      builder.append(jid)
      builder.append("\" ")
    }
    builder.append("superfeedr:page=\"")
    builder.append(page)
    builder.append("\"")
    if (subscriptions != null && !subscriptions.isEmpty) {
      builder.append(">\n")
      import scala.collection.JavaConversions._
      for (subscription <- subscriptions) {
        builder.append(subscription)
      }
      builder.append("</subscriptions>")
    }
    else {
      builder.append("/>")
    }
    builder.toString
  }

  def getSubscriptions: Iterator[SubscriptionExtension] = if (subscriptions == null) null else subscriptions.iterator
  def getJid = jid
  def getPage = page
}