name := "scala-case-class-exporter"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.7"
libraryDependencies += "com.github.scopt" %% "scopt" % "3.4.0"
libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

resolvers += Resolver.sonatypeRepo("public")

fork in Test := true
