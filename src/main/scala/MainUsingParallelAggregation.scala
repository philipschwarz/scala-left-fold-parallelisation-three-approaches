import GetWarAndPeace.*

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.chaining.*

import scala.collection.parallel.CollectionConverters._

@main
def mainUsingParallelAggregation(): Unit =

  def findLinesContaining(keyword: String)(lines: Vector[String]): String =
    lines.par.aggregate("")(
      (concatenatedLines, line) =>
        if line.matches(s".*$keyword.*") then s"$concatenatedLines\n'$line'" else concatenatedLines,
      _ ++ _
    )

  Await
    .result(
      getWarAndPeace().map(findLinesContaining("fantastic")),
      Duration.Inf
    )
    .tap(lines => println(s"Here are the matching lines: $lines"))
