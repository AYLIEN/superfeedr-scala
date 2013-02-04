package org.superfeedr.provider

import java.util.List
import java.util.Map
import org.jivesoftware.smack.packet.PacketExtension
import org.superfeedr.extension.notification.ItemsExtension
import org.superfeedr.extension.notification.StatusExtension
import org.superfeedr.extension.notification.SuperfeedrEventExtension

class EventProvider extends EmbeddedExtensionProvider {
  protected def createReturnExtension(currentElement: String, currentNamespace: String, attMap: Map[String, String], content: List[_ <: PacketExtension]): PacketExtension = {
    new SuperfeedrEventExtension(content.get(0).asInstanceOf[StatusExtension], content.get(1).asInstanceOf[ItemsExtension])
  }
}