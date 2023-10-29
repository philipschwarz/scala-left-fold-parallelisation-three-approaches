import scala.io.Source

object GetWarAndPeace:

  def getLinesFromWarAndPeace(): Vector[String] =
    val specSrc = Source.fromURL("https://www.gutenberg.org/cache/epub/2600/pg2600.txt")
    try specSrc.getLines.toVector
    finally specSrc.close