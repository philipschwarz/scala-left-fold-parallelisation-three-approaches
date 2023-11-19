package common

import cats.Functor

import java.net.URL
import scala.io.Source
import scala.util.{Try, Using}

val numberOfCores = Runtime.getRuntime().availableProcessors()

case class Book(name: String, numberOfLines: Int, numberOfBytes: Int, url: URL)

val theBible = Book(
  name = "The King James Version of the Bible",
  numberOfLines = 99_975,
  numberOfBytes = 4_456_041,
  url = URL("https://gutenberg.org/cache/epub/10/pg10.txt")
)

def getText(book: Book, copies: Int = 1): Try[Vector[String]] =
  Using(Source.fromURL(book.url)): source =>
    val lines = source.getLines.toVector
    Vector.fill(copies)(lines).flatten

def handleErrorGettingText[A](error: Throwable): A =
  throw IllegalStateException(s"Failed to obtain the text lines to be searched.", error)

def announceSuccessGettingText(lines: Vector[String]): Unit =
  println(f"Successfully obtained ${lines.length}%,d lines of text to search.")

def announceMatchingLines(lines: String): Unit =
  println(f"Found the word in the following ${lines.count(_ == '\n')}%,d lines of text: $lines")

def accumulateLinesContaining(word: String): (String, String) => String =
  (acc, line) => if line.matches(s".*$word.*") then s"$acc\n'$line'" else acc

import cats.syntax.functor.*
extension [A, F[_] : Functor](fa: F[A])
  def printThreadName(): F[A] =
    for
      a <- fa
      _ = println(s"[${Thread.currentThread().getName}]")
    yield a