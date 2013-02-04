package org.superfeedr.extension.subscription

import org.jivesoftware.smack.packet.PacketExtension

object PseudoPubSubPacketExtension {
  final val ELEMENT_NAME: String = "pubsub"
  final val NAMESPACE: String = "http://jabber.org/protocol/pubsub"
}

abstract class PseudoPubSubPacketExtension extends PacketExtension {
  def getElementName: String = PseudoPubSubPacketExtension.ELEMENT_NAME

  def getNamespace: String = PseudoPubSubPacketExtension.NAMESPACE

  def toXML: String = {
    val builder: StringBuilder = new StringBuilder("<")
    builder.append(getElementName)
    builder.append(" xmlns=\"")
    builder.append(getNamespace)
    builder.append("\">\n")
    builder.append(toXMLInternal)
    builder.append("</")
    builder.append(getElementName)
    builder.append(">")

    builder.toString
  }

  protected def toXMLInternal: String
}