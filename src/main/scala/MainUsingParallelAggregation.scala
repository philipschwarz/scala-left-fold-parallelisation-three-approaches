import common.*

import scala.collection.parallel.CollectionConverters.*

@main
def mainUsingParallelAggregation(): Unit =

  getLinesFromWarAndPeaceBook()
    .fold(
      error => handleUnsuccessfulDownload(error),
      lines =>
        announceSuccessfulDownload(lines)
        val matches = find(word = "fantastic", lines)
        announceMatchingLines(matches)
    )

  def find(word: String, lines: Vector[String]): String =
    lines.par.aggregate("")(seqop = accumulateLinesContaining(word), combop = _++_)
