package org.superfeedr.provider

import org.jivesoftware.smack.packet.PacketExtension
import org.superfeedr.extension.notification.ItemExtension
import org.superfeedr.extension.notification.ItemsExtension
import java.util

class ItemsProvider extends EmbeddedExtensionProvider {
  protected def createReturnExtension(currentElement: String, currentNamespace: String, attributeMap: util.Map[String, String], content: util.List[_ <: PacketExtension]): PacketExtension = {
    new ItemsExtension(attributeMap.get("node"), content.asInstanceOf[util.List[ItemExtension]])
  }
}