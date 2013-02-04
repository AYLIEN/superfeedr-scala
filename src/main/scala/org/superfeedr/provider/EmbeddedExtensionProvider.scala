package org.superfeedr.provider

import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map
import org.jivesoftware.smack.packet.PacketExtension
import org.jivesoftware.smack.provider.PacketExtensionProvider
import org.jivesoftware.smack.util.PacketParserUtils
import org.xmlpull.v1.XmlPullParser

abstract class EmbeddedExtensionProvider extends PacketExtensionProvider {
  final def parseExtension(parser: XmlPullParser): PacketExtension = {
    val namespace = parser.getNamespace
    val name = parser.getName

    val attMap: Map[String, String] = new HashMap[String, String]

    for (i <- (0 to parser.getAttributeCount - 1)) {
      attMap.put(parser.getAttributeName(i),parser.getAttributeValue(i))
    }

    val extensions: List[PacketExtension] = new ArrayList[PacketExtension]

    do {
      val tag: Int = parser.next

      if (tag == XmlPullParser.START_TAG) {
        val extensionName = parser.getName
        val extensionNamespace = parser.getNamespace
        extensions.add(PacketParserUtils.parsePacketExtension(extensionName, extensionNamespace, parser))
      }
    } while (!name.equals(parser.getName))

    createReturnExtension(name, namespace, attMap, extensions)
  }

  protected def createReturnExtension(currentElement: String, currentNamespace: String, attributeMap: Map[String, String], content: List[_ <: PacketExtension]): PacketExtension
}