package org.superfeedr.provider

import org.jivesoftware.smack.packet.PacketExtension
import org.superfeedr.extension.subscription.SubscriptionFeedExtension
import java.net.URL
import java.util

class SubscribeProvider extends EmbeddedExtensionProvider {
  protected def createReturnExtension(currentElement: String, currentNamespace: String, attributeMap: util.Map[String, String], content: util.List[_ <: PacketExtension]): PacketExtension = {
    val jid = attributeMap.get("jid")
    val nodeId = attributeMap.get("node")

    new SubscriptionFeedExtension(jid,new URL(nodeId))
  }
}
