package org.superfeedr.extension.subscription

import java.net.URL
import org.jivesoftware.smack.packet.PacketExtension

abstract class SubUnSubFeedExtension(val jid: String, val feedURL: URL) extends PacketExtension {
  def getNamespace: String = null

  def toXML: String = {
    val builder: StringBuilder = new StringBuilder("<")
    builder.append(getElementName)
    builder.append(" node=\"")
    builder.append(feedURL.toString)
    builder.append("\" jid=\"")
    builder.append(jid)
    builder.append("\"/>\n")

    builder.toString
  }
}