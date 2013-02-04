package org.superfeedr.provider

import org.jivesoftware.smack.packet.PacketExtension
import org.superfeedr.extension.subscription.SubscriptionExtension
import java.net.URL
import java.util

class SubscriptionProvider extends EmbeddedExtensionProvider {
  protected def createReturnExtension(currentElement: String, currentNamespace: String, attributeMap: util.Map[String, String], content: util.List[_ <: PacketExtension]): PacketExtension = {
    val jid = attributeMap.get("jid")
    val nodeId = attributeMap.get("node")
    val subId = attributeMap.get("subid")

    new SubscriptionExtension(new URL(nodeId),subId,jid)
  }
}
