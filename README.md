Scala wrapper for Superfeedr
============================

Usage
-----

1. Add the jar file to your classpath.
2. Then in your code:

```scala
XMPPConnection.DEBUG_ENABLED = true

val feedr = new Superfeedr("USERNAME", "PASSWORD", "superfeedr.com")

val feeds = List("http://feeds.bbci.co.uk/news/rss.xml",
                      "http://rss.cnn.com/rss/edition.rss")

feedr.subscribe(feeds.map(new URL(_)), new OnResponseHandler() {
  def onSuccess(response:Packet) {
    val subs = response.getExtensions.toList.asInstanceOf[List[SubscriptionFeedExtension]]
    subs.foreach(x=>println("Subscribed to: "+x.feedURL))
  }

  def onError(infos:String) {
  }
})

feedr.addOnNotificationHandler(notifHandler)

def notifHandler = new OnNotificationHandler {
  def onNotification(event:SuperfeedrEventExtension) {
    if (event.getItems.getItemsCount == 0) {
      println("No items")
    } else {
      println("Feed has %s items:".format(event.getItems.getItemsCount))
      for (item <- event.getItems.getItems) {
        val entry = item.getEntry
        println("- title: \n %s \n - link: \n %s \n - desc: \n %s".format(entry.title,entry.links(0).href,entry.summary))
      }
    }
    println("\n")
  }
}
```

see `TestApp.scala` for a working example.

Development
-----------

Use **SBT**:
- `test` task to run tests
- `package` task to bundle as Jar (`smack.providers` should be present in `META-INF` dir)
