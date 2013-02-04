package org.superfeedr.provider

import org.jivesoftware.smack.packet.PacketExtension
import org.jivesoftware.smack.provider.PacketExtensionProvider
import org.superfeedr.extension.notification.HttpExtension
import org.xmlpull.v1.XmlPullParser

class HttpProvider extends PacketExtensionProvider {
  def parseExtension(parser: XmlPullParser): PacketExtension = {
    val extensionName: String = parser.getName
    val code: String = parser.getAttributeValue(null, "code")
    var infos: String = null
    var currentTag: Int = parser.next

    while (!(extensionName == parser.getName) && currentTag != XmlPullParser.END_TAG) {
      if (currentTag == XmlPullParser.TEXT) {
        infos = parser.getText
      }
      currentTag = parser.next
    }
    new HttpExtension(code, infos)
  }
}