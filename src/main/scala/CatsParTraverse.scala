import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.foldable.*
import cats.syntax.parallel.*

object CatsParTraverse extends IOApp:

  override def run(args: List[String]): IO[ExitCode] =
    val word = args.headOption.getOrElse("incredible")
    runUsingCatsParTraverse(word).as(ExitCode.Success)

  def runUsingCatsParTraverse(word: String): IO[Unit] =
    IO.blocking(getText(book = theBible,copies = 1_000))
      .flatMap(
        _.fold(
          error => IO(handleErrorGettingText(error)),
          lines =>
            IO(announceSuccessGettingText(lines)) *>
              find(word, lines)
                .map(matches => announceMatchingLines(matches))
        )
      )

  def find(word: String, lines: Vector[String]): IO[String] =
    val batchSize = lines.size / (numberOfCores / 2)
    val groupsOfLines = lines.grouped(batchSize).toVector
    groupsOfLines
      .parTraverse(searchFor(word))
      .map(_.combineAll)
  
  def searchFor(word: String)(lines: Vector[String]): IO[String] =
    IO(lines.foldLeft("")(accumulateLinesContaining(word))).printThreadName()


