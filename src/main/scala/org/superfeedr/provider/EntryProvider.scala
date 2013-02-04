package org.superfeedr.provider

import org.jivesoftware.smack.provider.PacketExtensionProvider
import org.xmlpull.v1.XmlPullParser
import org.jivesoftware.smack.packet.PacketExtension
import org.superfeedr.extension.notification.{EntryLink, EntryExtension}
import java.util.Date
import collection.mutable.ListBuffer
import org.superfeedr.Superfeedr

class EntryProvider extends PacketExtensionProvider {
  def parseExtension(parser: XmlPullParser): PacketExtension = {

    val name = parser.getName
    var eventType = parser.next

    var id:String = null
    val links:ListBuffer[EntryLink] = ListBuffer()
    var published:Date = null
    var updated:Date = null
    var summary:String = null
    var title:String = null
    var content:String = null

    while (!name.equals(parser.getName)) {
      if(eventType == XmlPullParser.START_TAG) {

        parser.getName match {
          case "title" => title = parser.nextText
          case "summary" => summary = parser.nextText
          case "content" => content = parser.nextText
          case "id" => id = parser.nextText
          case "published" => published = Superfeedr.convertDate(parser.nextText())
          case "updated" => updated = Superfeedr.convertDate(parser.nextText())
          case "link" => {
            var href:String = null
            var title:String = ""
            var linkType:String = ""
            var rel:String = ""

            for (i <- (0 to parser.getAttributeCount - 1)) {
              parser.getAttributeName(i) match {
                case "href" => href = parser.getAttributeValue(i)
                case "type" => linkType = parser.getAttributeValue(i)
                case "title" => title = parser.getAttributeValue(i)
                case "rel" => rel = parser.getAttributeValue(i)
                case _ =>
              }
            }

            links += EntryLink(href,rel,linkType,title)
          }
          case _ =>
        }
      }

      eventType = parser.next()
    }

    new EntryExtension(id,links.toList,published,updated,summary,title,content)
  }
}
