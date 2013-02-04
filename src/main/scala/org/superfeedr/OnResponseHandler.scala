package org.superfeedr

import org.jivesoftware.smack.packet.Packet

trait OnResponseHandler {
  def onSuccess(response: Packet)

  def onError(infos: String)
}