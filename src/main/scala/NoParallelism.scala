import common.*

@main def runWithoutParallelism(word: String = "incredible"): Unit =

  println(System.currentTimeMillis())
  getText(book = theBible, copies = 1_000)
    .fold(
      error => handleErrorGettingText(error),
      lines =>
        println(System.currentTimeMillis())
        announceSuccessGettingText(lines)
        val matches = find(word, lines)
        announceMatchingLines(matches)
    )

  def find(word: String, lines: Vector[String]): String =
    lines.foldLeft("")(accumulateLinesContaining(word))