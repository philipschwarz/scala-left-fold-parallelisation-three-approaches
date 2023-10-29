import Common.*

import scala.collection.parallel.CollectionConverters.*
import scala.util.chaining.*

@main
def mainUsingParallelAggregation(): Unit =

  find(word = "fantastic", lines = getLinesFrom(bookURL = warAndPeaceURL))
    .tap(announceMatchingLines)

  def find(word: String, lines: Vector[String]): String =
    lines.par.aggregate("")(
      seqop = accumulateLinesContaining(word),
      combop = _ ++ _
    )
