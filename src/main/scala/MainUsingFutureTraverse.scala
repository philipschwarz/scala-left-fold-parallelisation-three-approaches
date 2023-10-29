import Common.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.chaining.*
import cats.syntax.foldable.*

@main
def mainUsingFutureTraverse(): Unit =

  find(word = "fantastic", lines = getLinesFrom(bookURL = warAndPeaceURL))
    .tap(announceMatchingLines)

  def find(word: String, lines: Vector[String]): String =
    Await.result(
      Future.traverse(lines.grouped(10_000).toList)(searchFor(word)),
      Duration.Inf
    ).combineAll

  def searchFor(word: String)(lines: Vector[String]): Future[String] =
    Future(lines.foldLeft("")(accumulateLinesContaining(word)))