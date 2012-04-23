import scala.xml.{ Elem, XML }
import scala.xml.factory.XMLLoader
import java.net.URL
import scala.actors.Actor
import scala.util.matching.Regex
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import java.io.IOException
import scala.collection.immutable.TreeSet

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
case class HTTPLink(address: String)
case class Link(address: String)
case class Done()

class CrawlControl extends Actor {
   
  def printData(mails: List[String], links: List[String]) {
    println("Finished gathering: " + links.length + " links")
    links foreach println
    mails foreach println
  }
  
  def act() {
    def loop(running: Int, seen: TreeSet[String], mails: List[String], links: List[String]) {
      react {
        case MailTo(address)   ⇒ loop(running, seen, address :: mails, links)

        case HTTPLink(address) ⇒ {
          if ((links.length % 100) == 0) {
            println("Links gathered: " + links.length)
          }
          loop(running, seen, mails, address :: links)
        }

        case Link(address) ⇒ {
          if (links.length < 1000) {
            if (!(seen contains address)) {
              new Crawler(address, this).start()
              loop(running + 1, seen + address, mails, links)
            }
            loop(running, seen, mails, links)
          } else {
            printData(mails, links)
          }
        }

        case Done() ⇒ {
          if (running > 1)
            loop(running - 1, seen, mails, links)
          else
        	printData(mails, links)
        }

        case _ ⇒ {
          println("Missbehaving children")
        }

      }
    }
    loop(0, TreeSet(), List(), List())
  }
}

class Crawler(val link: String, control: Actor) extends Actor {
  def act() {
  
    val url = new URL(link)
    try {
      val connection = url.openConnection()
      connection setConnectTimeout 500
      connection setReadTimeout 1000
      
      if (link startsWith "mailto:") {
        control ! MailTo(link)
      } else if(connection.getContentType contains "text/html") {
        control ! HTTPLink(link)
      } else {
        return
      }
     
      val html = TagSoupXmlLoader.htmlParser load connection.getInputStream

      for (link ← (html \\ "@href" ++ html \\ "@src")) {
        val address = new URL(url, link.text)
        control ! Link(address.toString)
      }
    } catch {
      case ioe: IOException ⇒ {}
      case e: Exception     ⇒ {}//e.printStackTrace();  println("This one broke: " + url.toString())}
    } finally {
      control ! Done()
    }
  }
}

object Main extends App {

  val control = new CrawlControl
  control.start

  Actor.actor {
    control ! Link("http://cs.lth.se/kurs/eda095")
  }

}