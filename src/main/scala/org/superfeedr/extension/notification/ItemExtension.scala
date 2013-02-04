package org.superfeedr.extension.notification

class ItemExtension(val entryExtension: EntryExtension) extends DefaultSuperfeedrExtension {
  def getEntry: EntryExtension = entryExtension
}