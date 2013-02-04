package org.superfeedr.extension.subscription

import java.net.URL

object UnSubscriptionFeedExtension {
  final val ELEMENT_NAME: String = "unsubscribe"
}

class UnSubscriptionFeedExtension(override val jid: String, override val feedURL: URL) extends SubUnSubFeedExtension(jid, feedURL) {
  def getElementName: String = UnSubscriptionFeedExtension.ELEMENT_NAME
}