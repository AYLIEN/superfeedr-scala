package org.superfeedr.provider

import org.jivesoftware.smack.packet.PacketExtension
import org.superfeedr.extension.notification.EntryExtension
import org.superfeedr.extension.notification.ItemExtension
import java.util

class ItemProvider extends EmbeddedExtensionProvider {
  protected def createReturnExtension(currentElement: String, currentNamespace: String, attributeMap: util.Map[String, String], content: util.List[_ <: PacketExtension]): PacketExtension = {
    new ItemExtension(content.get(0).asInstanceOf[EntryExtension])
  }
}