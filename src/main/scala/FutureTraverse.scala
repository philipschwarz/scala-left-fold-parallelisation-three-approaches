import common.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@main
def runUsingFutureTraverse: Unit =

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
        Future.traverse(lines.grouped(15_000).toList)(searchFor(word))
              .map(_.foldLeft("")(_++_)),
        Duration.Inf
      )

  def searchFor(word: String)(lines: Vector[String]): Future[String] =
    Future(lines.foldLeft("")(accumulateLinesContaining(word)))
