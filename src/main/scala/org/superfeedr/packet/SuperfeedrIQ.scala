package org.superfeedr.packet

import org.jivesoftware.smack.packet.IQ

class SuperfeedrIQ(var xmlChild: String = null) extends IQ {
  def getChildElementXML: String = xmlChild
}