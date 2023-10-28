import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

object GetWarAndPeace:

  def getWarAndPeace(): Future[Vector[String]] = Future:
    val specSrc = Source.fromURL("https://www.gutenberg.org/cache/epub/2600/pg2600.txt")
    try specSrc.getLines.toVector
    finally specSrc.close