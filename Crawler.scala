import scala.xml.{ Elem, XML }
import scala.xml.factory.XMLLoader
import java.net.URL
import scala.actors.Actor
import scala.util.matching.Regex
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import java.io.IOException

object TagSoupXmlLoader {
  private val factory = new SAXFactoryImpl()
  def htmlParser() = XML.withSAXParser(factory.newSAXParser())
}

/**
 * ***************************************************************************
 * ********************************* Crawler **********************************
 * ***************************************************************************
 */

case class MailTo(address: String)
case class HTTPLink(url: String)

class CrawlControl extends Actor {
  def act() {
    def loop(mails: List[String], links: List[String]) {
      react {
        case MailTo(address) ⇒
          if (!(mails contains address))
            loop(address :: mails, links)
          else
            loop(mails, links)

        case HTTPLink(address) ⇒ {
          if (links.length < 1000) {
            if (!(links contains address)) {
              new Crawler(address, this).start()
              println(links.length)
              loop(mails, address :: links)
            }
            loop(mails, links)
          } else {
            println("Finished! " + links.length)
            links foreach println
            mails foreach println
          }
        }

        case _ ⇒ {
          println("Missbehaving children")
        }

      }
    }
    loop(List(), List())
  }
}

class Crawler(val link: String, control: Actor) extends Actor {
  def act() {

    val url = new URL(link)
    try {
      val connection = url.openConnection()
      connection setConnectTimeout 1000
      //connection.setReadTimeout(2000)

      val html = TagSoupXmlLoader.htmlParser load connection.getInputStream

      for (link ← (html \\ "@href" ++ html \\ "@src")) {
        val address = link.text
        
        if (address.startsWith("mailto:")) {
          control ! MailTo(address)
        } else {
          val newUrl = new URL(url, address)
          val newConnection = newUrl.openConnection()
          newConnection setConnectTimeout 1000
          
          println(newUrl.toString())

          if (newConnection.getContentType contains "text/html") {
            control ! HTTPLink(newUrl.toString())
          }
        }
      }
    } catch {
      case ioe: IOException ⇒ {}
      case e: Exception => {println(url.toString())}
    }
  }
}

object Main extends App {

  val control = new CrawlControl
  control.start

  Actor.actor {
    val future = control !! HTTPLink("https://github.com/")
    future()
  }

}