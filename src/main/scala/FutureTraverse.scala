import common.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@main def runUsingFutureTraverse(word: String = "incredible"): Unit =
  getText(book = theBible, copies = 1_000)
    .fold(
      error => handleErrorGettingText(error),
      lines =>
        announceSuccessGettingText(lines)
        val matches = find(word, lines)
        announceMatchingLines(matches)
    )

  def find(word: String, lines: Vector[String]): String =
    val batchSize = lines.size / (numberOfCores / 2)
    val groupsOfLines = lines.grouped(batchSize).toVector
    Await
      .result(
        Future.traverse(groupsOfLines)(searchFor(word))
              .map(_.foldLeft("")(_++_)),
        Duration.Inf
      )
  
  def searchFor(word: String)(lines: Vector[String]): Future[String] =
    Future(lines.foldLeft("")(accumulateLinesContaining(word))).printThreadName()