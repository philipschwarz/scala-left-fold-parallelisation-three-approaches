import GetWarAndPeace.*

import cats.effect.{IO, IOApp}

import cats.syntax.parallel.*
import cats.syntax.foldable.*

object MainUsingCatsParTraverse extends IOApp.Simple:

  override def run: IO[Unit] =
    IO( getLinesFromWarAndPeace() )
      .flatMap( findLinesContaining(word = "fantastic") )
      .flatMap( lines => IO.println(s"Here are the matching lines: $lines") )

  def findLinesContaining(word: String)(lines: Vector[String]): IO[String] =
    lines
      .grouped(10_000)
      .toVector
      .parTraverse(findAndConcatenateLinesContaining(word))
      .map(_.combineAll)

  def findAndConcatenateLinesContaining(keyword: String)(lines: Vector[String]): IO[String] =
    IO {
      lines.foldLeft("") { (acc, line) =>
        if line.matches(s".*$keyword.*") then s"$acc\n'$line'" else acc
      }
    }
