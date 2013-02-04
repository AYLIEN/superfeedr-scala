package org.superfeedr.extension.subscription

import org.jivesoftware.smack.packet.PacketExtension
import java.net.URL

object SubscriptionExtension {
  private final val ELEMENT_NAME = "subscription"
}

class SubscriptionExtension(var node: URL, var subscription: String, var jid: String) extends PacketExtension {
  def toXML: String = {
    val builder: StringBuilder = new StringBuilder("<")
    builder.append(SubscriptionExtension.ELEMENT_NAME)
    builder.append(" node=\"")
    builder.append(node)
    builder.append("\" subscription=\"")
    builder.append(subscription)
    builder.append("\" jid=\"")
    builder.append(jid)
    builder.append("\" />")

    builder.toString
  }

  def getNode = node
  def getSubscription = subscription
  def getJid = jid
  def getElementName = SubscriptionExtension.ELEMENT_NAME
  def getNamespace = null
}