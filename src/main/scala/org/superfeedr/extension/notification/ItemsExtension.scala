package org.superfeedr.extension.notification

import java.util

class ItemsExtension(val node: String, val items: util.List[ItemExtension]) extends DefaultSuperfeedrExtension {
  def getItems: util.Iterator[ItemExtension] = {
    if (items == null) new util.Iterator[ItemExtension] {
      def hasNext: Boolean = false
      def next: ItemExtension = null
      def remove() {}
    } else items.iterator
  }

  def getNode: String = node
  def getItemsCount: Int = if (items != null) items.size else 0
}