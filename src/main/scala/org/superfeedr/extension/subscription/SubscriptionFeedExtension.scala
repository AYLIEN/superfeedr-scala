package org.superfeedr.extension.subscription

import java.net.URL

object SubscriptionFeedExtension {
  final val ELEMENT_NAME: String = "subscribe"
}

class SubscriptionFeedExtension (override val jid: String, override val feedURL: URL) extends SubUnSubFeedExtension(jid, feedURL) {
  def getElementName: String = SubscriptionFeedExtension.ELEMENT_NAME
}