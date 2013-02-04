package org.superfeedr.extension.notification

class HttpExtension(val code: String, val info: String) extends DefaultSuperfeedrExtension {
  def getCode: String = code
  def getInfo: String = info
}