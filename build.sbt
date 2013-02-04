name := "Superfeedr Scala"

organization := "org.superfeedr"

version := "0.1.0"

scalaVersion := "2.9.2"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.12" % "test",
  "jivesoftware" % "smack" % "3.1.0",
  "jivesoftware" % "smackx" % "3.1.0"
)

initialCommands := "import com.aylien.superfeedrscala._"
