package org.superfeedr.extension.notification

import java.util.Date

class StatusExtension(var feedURL: String = null, var nextFetch: Date = null, var extension: HttpExtension = null) extends DefaultSuperfeedrExtension {
  def getFeedURL: String = feedURL
  def getHttpExtension: HttpExtension = extension
  def getNextFetch: Date = nextFetch
}