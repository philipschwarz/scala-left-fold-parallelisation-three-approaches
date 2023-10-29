import GetWarAndPeace.*

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.chaining.*

@main
def mainUsingFutureTraverse(): Unit =

  import scala.concurrent.ExecutionContext.Implicits.global

  def findLinesContaining(keyword: String)(lines: Vector[String]): Future[String] =
    Future
      .traverse(lines.grouped(1_000))(findAndConcatenateLinesContaining(keyword))
      .map(combine)

  def findAndConcatenateLinesContaining(keyword: String)(lines: Vector[String]): Future[String] =
    Future {
      lines.foldLeft("") {
        (acc, line) =>
         if line.matches(s".*$keyword.*") then s"$acc\n'$line'" else acc
      }
    }

  def combine: Iterator[String] => String = {
    _.foldLeft("")(_ ++ _)
  }

  Await
    .result(
      Future{ getLinesFromWarAndPeace() }.flatMap(findLinesContaining("fantastic")),
      Duration.Inf
    )
    .tap(lines => println(s"Here are the matching lines: $lines"))
