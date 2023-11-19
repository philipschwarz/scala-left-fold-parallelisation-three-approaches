import scala.collection.parallel.CollectionConverters.*

@main def runUsingParallelAggregation(word: String = "incredible"): Unit =

  getText(book = theBible, copies = 1_000)
    .fold(
      error => handleErrorGettingText(error),
      lines =>
        announceSuccessGettingText(lines)
        val matches = find(word, lines)
        announceMatchingLines(matches)
    )

  def find(word: String, lines: Vector[String]): String =
    lines.par.aggregate("")(seqop = accumulateLinesContaining(word), combop = _++_)