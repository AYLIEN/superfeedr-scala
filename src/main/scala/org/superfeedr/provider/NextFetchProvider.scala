package org.superfeedr.provider

import org.jivesoftware.smack.packet.DefaultPacketExtension
import org.jivesoftware.smack.packet.PacketExtension
import org.jivesoftware.smack.provider.PacketExtensionProvider
import org.xmlpull.v1.XmlPullParser

class NextFetchProvider extends PacketExtensionProvider {
  def parseExtension(parser: XmlPullParser): PacketExtension = {
    val extension: DefaultPacketExtension = new DefaultPacketExtension(parser.getName, parser.getNamespace)
    extension.setValue("next", parser.nextText)
    extension
  }
}