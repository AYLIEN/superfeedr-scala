package org.superfeedr.extension.notification

import org.jivesoftware.smack.packet.PacketExtension

class DefaultSuperfeedrExtension extends PacketExtension {
  def getElementName: String = null
  def getNamespace: String = null
  def toXML: String = null
}