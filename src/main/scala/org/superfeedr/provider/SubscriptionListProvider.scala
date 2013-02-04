package org.superfeedr.provider

import java.util.List
import java.util.Map
import org.jivesoftware.smack.packet.PacketExtension
import org.superfeedr.extension.subscription.{SubscriptionExtension, SubscriptionListExtension}
import scala.collection.JavaConversions._

class SubscriptionListProvider extends EmbeddedExtensionProvider {
  protected def createReturnExtension(currentElement: String, currentNamespace: String, attributeMap: Map[String, String], content: List[_ <: PacketExtension]): PacketExtension = {
    val jid = attributeMap.get("jid")
    val page = attributeMap.get("page")

    new SubscriptionListExtension(content.asInstanceOf[List[SubscriptionExtension]], jid, page.toLong)
  }
}