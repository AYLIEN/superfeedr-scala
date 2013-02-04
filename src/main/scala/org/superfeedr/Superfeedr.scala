package org.superfeedr

import java.net.URL
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.List
import java.util.Map
import org.jivesoftware.smack.PacketListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.filter.OrFilter
import org.jivesoftware.smack.filter.PacketTypeFilter
import org.jivesoftware.smack.packet.IQ
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Packet
import org.jivesoftware.smack.packet.PacketExtension
import org.jivesoftware.smack.packet.XMPPError
import org.jivesoftware.smack.packet.IQ.Type
import org.superfeedr.extension.notification.SuperfeedrEventExtension
import extension.subscription.{SubscriptionExtension, SubUnSubExtension, SubscriptionListExtension}
import org.superfeedr.packet.SuperfeedrIQ
import org.jivesoftware.smack.provider.IQProvider
import org.xmlpull.v1.XmlPullParser
import org.jivesoftware.smack.util.PacketParserUtils
import scala.collection.JavaConversions._

object Superfeedr {
  /**
   * Utility function for parsing dates
   * @param date
   * @return
   */
  def convertDate(date: String): Date = {
    try {
      m_ISO8601Local.parse(date)
    }
    catch {
      case e: ParseException => {
        e.printStackTrace()
        return new Date
      }
    }
  }

  private final val FIREHOSER: String = "firehoser.superfeedr.com"
  final val DEFAULT_RESOURCE: String = "superfeedr-java"
  private val m_ISO8601Local: DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
}

class Superfeedr(val jid: String, val password: String, val server: String, val resource: String = Superfeedr.DEFAULT_RESOURCE) {
  private var connection: XMPPConnection = null
  private val onNotificationHandlers: ArrayList[OnNotificationHandler] = new ArrayList[OnNotificationHandler]
  private val pendingOnReponseHandlers: Map[String, OnResponseHandler] = new HashMap[String, OnResponseHandler]

  if (jid == null || password == null || server == null || resource == null) throw new IllegalArgumentException("arguments cannot be null")

  connection = new XMPPConnection(server)

  try {
    connection.connect()
    connection.login(jid, password, resource)
    connection.addPacketListener(new SuperFeedrPacketListener, new OrFilter(new PacketTypeFilter(classOf[Message]), new PacketTypeFilter(classOf[IQ])))
  }
  catch {
    case e: XMPPException => {
      if (connection != null && connection.isConnected) {
        connection.disconnect()
      }
      throw e
    }
  }

  /**
   * Close connection to Superfeedr
   */
  def close() {
    if (connection != null && connection.isConnected) {
      connection.disconnect()
    }
  }

  /**
   * Add a notification handler to listen for notification events
   * @param handler
   */
  def addOnNotificationHandler(handler: OnNotificationHandler) {
    if (handler != null && !onNotificationHandlers.contains(handler)) {
      onNotificationHandlers.add(handler)
    }
  }

  private def fireOnNotificationHandlers(event: SuperfeedrEventExtension) {
    for (handler <- onNotificationHandlers) {
      handler.onNotification(event)
    }
  }

  /**
   * Remove a notification handler
   * @param handler
   */
  def removeOnNotificationHandler(handler: OnNotificationHandler) {
    onNotificationHandlers.remove(handler)
  }

  private def subUnsubscribe(subUnsubscription: SubUnSubExtension, handler: OnResponseHandler) {
    val iq: SuperfeedrIQ = new SuperfeedrIQ(subUnsubscription.toXML)
    iq.setTo(Superfeedr.FIREHOSER)
    iq.setType(Type.SET)
    connection.sendPacket(iq)
    pendingOnReponseHandlers.put(iq.getPacketID, handler)
  }

  /**
   * Subscribe to a list of feed URLs and add a notification handler to be notified when there are new stories
   * @param feedUrls
   * @param handler
   */
  def subscribe(feedUrls: List[URL], handler: OnResponseHandler) {
    subUnsubscribe(new SubUnSubExtension(feedUrls, jid + "@" + server, SubUnSubExtension.TYPE_SUBSCRIPTION), handler)
  }

  /**
   * Unsubscribe from a list of feed URLs
   * @param feedUrls
   * @param handler
   */
  def unsubscribe(feedUrls: List[URL], handler: OnResponseHandler) {
    subUnsubscribe(new SubUnSubExtension(feedUrls, jid + "@" + server, SubUnSubExtension.TYPE_UNSUBSCRIPTION), handler)
  }

  /**
   * Get a list of subscriptions
   * @param handler
   */
  def getSubscriptionList(handler: OnResponseHandler) {
    val list: SubscriptionListExtension = new SubscriptionListExtension(null, jid + "@" + server, 1)
    val iq: SuperfeedrIQ = new SuperfeedrIQ(list.toXML)
    iq.setTo(Superfeedr.FIREHOSER)
    iq.setType(Type.GET)
    connection.sendPacket(iq)
    pendingOnReponseHandlers.put(iq.getPacketID, handler)
  }

  /**
   * Unsubscribe from all subscribed feeds
   */
  def unsubscribeAllFeeds(handler:OnResponseHandler) {
    getSubscriptionList(new OnResponseHandler {
      def onError(infos: String) {
        throw new Exception("Unsubscription failed: \n"+infos)
      }

      def onSuccess(response: Packet) {
        val subs = response.getExtensions.head.asInstanceOf[SubscriptionListExtension].getSubscriptions.toList.map(_.getNode)
        unsubscribe(subs, handler)
      }
    })
  }

  private class SuperFeedrPacketListener extends PacketListener {
    def processPacket(packet: Packet) {
      if (packet.isInstanceOf[Message]) {
        fireOnNotificationHandlers((packet.asInstanceOf[Message]).getExtension(SuperfeedrEventExtension.NAMESPACE).asInstanceOf[SuperfeedrEventExtension])
      } else {
        val packetID: String = packet.getPacketID
        val handler: OnResponseHandler = pendingOnReponseHandlers.get(packetID)

        if (handler != null) {
          pendingOnReponseHandlers.remove(packetID)
          val error: XMPPError = packet.getError
          if (error != null) {
            val builder: StringBuilder = new StringBuilder(error.getCondition)
            builder.append("Type = \n")
            builder.append(error.getType.name)
            builder.append("\n")
            val extensions: List[PacketExtension] = error.getExtensions
            for (packetExtension <- extensions) {
              builder.append(packetExtension.getElementName)
              builder.append("\n")
            }
            handler.onError(builder.toString)
          } else {
            handler.onSuccess(packet)
          }
        }
      }
    }
  }
}

class PubSubProvider extends IQProvider {
  def parseIQ(parser: XmlPullParser): IQ = {
    val ret = new SuperfeedrIQ
    val namespace = parser.getNamespace

    var done = false

    while (!done) {
      val eventType = parser.next
      if (eventType == XmlPullParser.START_TAG) {
        val ext = PacketParserUtils.parsePacketExtension(parser.getName, namespace, parser)
        ret.addExtension(ext)
      } else if (eventType == XmlPullParser.END_TAG) {
        if (parser.getName.equals("pubsub")) done = true
      }
    }
    ret
  }
}