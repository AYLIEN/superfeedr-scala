package org.superfeedr

import org.superfeedr.extension.notification.SuperfeedrEventExtension

trait OnNotificationHandler {
  def onNotification(event: SuperfeedrEventExtension)
}