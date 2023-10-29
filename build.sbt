ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"



lazy val root = (project in file("."))
  .settings(
    name := "future-traverse-parallel-aggregat-applicative-parTraverse",
    javaOptions += "-dcats.effect.warnOnNonMainThreadDetected=false",
  )

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.2"
