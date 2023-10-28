import GetWarAndPeace.*

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import cats.effect.{ExitCode, IO, IOApp}

import cats.syntax.parallel._
import cats.syntax.foldable._

import scala.concurrent.duration.Duration

object Main extends IOApp:

  override def run(args: List[String]): IO[ExitCode] =
    IO.fromFuture(IO(getWarAndPeace()))
      .map(findLinesContaining("fantastic"))
      .flatTap(lines => IO(println(s"Here are the matching lines: $lines")))
      .as(ExitCode.Success)

  def findLinesContaining(keyword: String)(lines: Vector[String]): IO[String] =
    lines
      .grouped(10_000)
      .toVector
      .parTraverse(findAndConcatenateLinesContaining(keyword))
      .map(_.combineAll)

  def findAndConcatenateLinesContaining(keyword: String)(lines: Vector[String]): IO[String] =
    IO {
      lines.foldLeft("") { (concatenatedLines, line) =>
        if line.matches(s".*$keyword.*") then s"$concatenatedLines\n'$line'" else concatenatedLines
      }
    }
