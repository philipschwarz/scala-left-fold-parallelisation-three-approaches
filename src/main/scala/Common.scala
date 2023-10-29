import scala.io.Source
import scala.util.Using
import scala.util.chaining.*

object Common:

  val warAndPeaceURL = "https://www.gutenberg.org/cache/epub/2600/pg2600.txt"

  /**
   * Download the lines of text in 'War and Peace'
   * @return the lines
   * @throws java.lang.IllegalStateException if there is an error downloading 'War and Peace'
   */
  def getLinesFrom(bookURL:  String): Vector[String] =
    Using(Source.fromURL(bookURL)) { source =>
      source.getLines.toVector
    }.fold(
        error => handleUnsuccessfulDownload(error),
        lines => lines.tap(announceSuccessfulDownload)
    )

  def handleUnsuccessfulDownload(error: Throwable): Vector[String] =
    throw IllegalStateException("Failed to download 'War and Peace':", error)

  def announceSuccessfulDownload(lines: Vector[String]): Unit =
    println(f"Successfully downloaded 'War and Peace', which is ${lines.length}%,d lines long.")

  def announceMatchingLines(lines: String): Unit =
    println(f"Found the word in the following ${lines.count(_ == '\n')} lines: $lines")

  def accumulateLinesContaining(word: String): (String, String) => String =
    (acc, line) => if line.matches(s".*$word.*") then s"$acc\n'$line'" else acc