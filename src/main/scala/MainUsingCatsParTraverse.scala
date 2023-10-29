import Common.*
import cats.effect.{IO, IOApp}
import cats.syntax.foldable.*
import cats.syntax.parallel.*

object MainUsingCatsParTraverse extends IOApp.Simple:

  override def run: IO[Unit] =
    IO(getLinesFrom(warAndPeaceURL))
      .flatMap(find(word = "fantastic"))
      .flatMap(lines => IO(announceMatchingLines(lines)))

  def find(word: String)(lines: Vector[String]): IO[String] =
    lines
      .grouped(10_000)
      .toVector
      .parTraverse(searchFor(word))
      .map(_.combineAll)

  def searchFor(word: String)(lines: Vector[String]): IO[String] =
    IO(lines.foldLeft("")(accumulateLinesContaining(word)))
