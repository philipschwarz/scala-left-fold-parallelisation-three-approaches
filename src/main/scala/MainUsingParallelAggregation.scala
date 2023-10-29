import GetWarAndPeace.*

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.chaining.*

import scala.collection.parallel.CollectionConverters._

@main
def mainUsingParallelAggregation(): Unit =
  import scala.concurrent.ExecutionContext.Implicits.global

  def findLinesContaining(keyword: String)(lines: Vector[String]): String =
    lines.par.aggregate("")(
      (acc, line) =>
        if line.matches(s".*$keyword.*") then s"$acc\n'$line'" else acc,
      _ ++ _
    )

  Await
    .result(
      Future{ getLinesFromWarAndPeace() }.map(findLinesContaining("fantastic")),
      Duration.Inf
    )
    .tap(lines => println(s"Here are the matching lines: $lines"))
