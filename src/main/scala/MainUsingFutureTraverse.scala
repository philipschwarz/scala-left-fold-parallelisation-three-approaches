import Common.*
import cats.syntax.foldable.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.chaining.*

@main
def mainUsingFutureTraverse(): Unit =

  getLinesFrom(bookURL = warAndPeaceURL)
    .fold(
      error => handleUnsuccessfulDownload(error),
      lines =>
        announceSuccessfulDownload(lines)
        val matches = find(word = "fantastic", lines)
        announceMatchingLines(matches)
    )

  def find(word: String, lines: Vector[String]): String =
    Await
      .result(
        Future.traverse(lines.grouped(10_000).toList)(searchFor(word)),
        Duration.Inf
      )
      .combineAll

  def searchFor(word: String)(lines: Vector[String]): Future[String] =
    Future(lines.foldLeft("")(accumulateLinesContaining(word)))
