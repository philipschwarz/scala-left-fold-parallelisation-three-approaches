import cats.effect.{IO, IOApp}
import cats.syntax.foldable.*
import cats.syntax.parallel.*
import common.*

object MainUsingCatsParTraverse extends IOApp.Simple:

  override def run: IO[Unit] =
    IO(getLinesFromWarAndPeaceBook())
      .flatMap( maybeLines =>
        maybeLines.fold(
          error => IO(handleUnsuccessfulDownload(error)),
          lines =>
            IO(announceSuccessfulDownload(lines)) *>
              find(word = "fantastic", lines)
                .map(matches => announceMatchingLines(matches))
        )
      )

  def find(word: String, lines: Vector[String]): IO[String] =
    lines
      .grouped(15_000)
      .toVector
      .parTraverse(searchFor(word))
      .map(_.combineAll)

  def searchFor(word: String)(lines: Vector[String]): IO[String] =
    IO(lines.foldLeft("")(accumulateLinesContaining(word)))
