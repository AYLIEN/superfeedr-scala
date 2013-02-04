package org.superfeedr.extension.notification

object SuperfeedrEventExtension {
  final val NAMESPACE: String = "http://jabber.org/protocol/pubsub#event"
  final val ELEMENT_NAME: String = "event"
}

class SuperfeedrEventExtension(val status: StatusExtension = null, val items: ItemsExtension = null) extends DefaultSuperfeedrExtension {
  override def getNamespace: String = SuperfeedrEventExtension.NAMESPACE
  override def getElementName: String = SuperfeedrEventExtension.ELEMENT_NAME

  def getStatus: StatusExtension = status
  def getItems: ItemsExtension = items
}