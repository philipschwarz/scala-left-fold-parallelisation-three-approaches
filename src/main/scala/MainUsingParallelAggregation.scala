import Common.*

import scala.collection.parallel.CollectionConverters.*
import scala.util.chaining.*

@main
def mainUsingParallelAggregation(): Unit =

  getLinesFrom(bookURL = warAndPeaceURL)
    .fold(
      error => handleUnsuccessfulDownload(error),
      lines =>
        announceSuccessfulDownload(lines)
        val matches = find(word = "fantastic", lines)
        announceMatchingLines(matches)
    )

  def find(word: String, lines: Vector[String]): String =
    lines.par.aggregate("")(
      seqop = accumulateLinesContaining(word),
      combop = _ ++ _
    )
