package org.superfeedr.extension.notification

import java.util.Date

case class EntryExtension(id:String, links:List[EntryLink], published:Date, updated:Date, summary:String, titles:List[String], content:String, author:Author) extends DefaultSuperfeedrExtension

case class EntryLink(href:String, rel:String = "", linkType:String = "", title:String = "")
case class Author(var name: String, var email: String)