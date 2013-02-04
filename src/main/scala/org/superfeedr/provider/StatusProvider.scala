package org.superfeedr.provider

import java.util.{Date, List, Map}
import org.jivesoftware.smack.packet.DefaultPacketExtension
import org.jivesoftware.smack.packet.PacketExtension
import org.superfeedr.Superfeedr
import org.superfeedr.extension.notification.HttpExtension
import org.superfeedr.extension.notification.StatusExtension

class StatusProvider extends EmbeddedExtensionProvider {
  protected def createReturnExtension(currentElement: String, currentNamespace: String, attributeMap: Map[String, String], content: List[_ <: PacketExtension]): PacketExtension = {
    val feedURL = attributeMap.get("feed")
//    val nextFetch= (content.get(1).asInstanceOf[DefaultPacketExtension])
//    new StatusExtension(feedURL, Superfeedr.convertDate(nextFetch.getValue("next")), content.get(0).asInstanceOf[HttpExtension])
    new StatusExtension(feedURL, new Date, content.get(0).asInstanceOf[HttpExtension])
  }
}