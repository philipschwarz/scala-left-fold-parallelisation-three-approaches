package common

import scala.io.Source
import scala.util.{Try, Using}

def getLinesFromWarAndPeaceBook(): Try[Vector[String]] =
  Using(Source.fromURL("https://www.gutenberg.org/cache/epub/2600/pg2600.txt")) { source =>
    source.getLines.toVector
  }

def handleUnsuccessfulDownload[A](error: Throwable): A =
  throw IllegalStateException("Failed to download 'War and Peace':", error)

def announceSuccessfulDownload(lines: Vector[String]): Unit =
  println(f"Successfully downloaded 'War and Peace', which is ${lines.length}%,d lines long.")

def announceMatchingLines(lines: String): Unit =
  println(f"Found the word in the following ${lines.count(_ == '\n')} lines: $lines")

def accumulateLinesContaining(word: String): (String, String) => String =
  (acc, line) => if line.matches(s".*$word.*") then s"$acc\n'$line'" else acc