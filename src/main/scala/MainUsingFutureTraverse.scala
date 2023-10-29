import common.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@main
def mainUsingFutureTraverse(): Unit =

  getLinesFromWarAndPeaceBook()
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
        Future.traverse(lines.grouped(15_000).toList)(searchFor(word)),
        Duration.Inf
      )
      .foldLeft("")(_++_)

  def searchFor(word: String)(lines: Vector[String]): Future[String] =
    Future(lines.foldLeft("")(accumulateLinesContaining(word)))
