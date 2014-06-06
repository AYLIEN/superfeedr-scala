name := "Superfeedr Scala"

organization := "org.superfeedr"

version := "0.1.0"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2",
  "org.specs2" %% "specs2" % "2.3.12" % "test",
  "jivesoftware" % "smack" % "3.1.0",
  "jivesoftware" % "smackx" % "3.1.0"
)

initialCommands := "import com.aylien.superfeedrscala._"

mappings in (Compile, packageBin) += {
  (baseDirectory.value / "src" / "META-INF" / "smack.providers") -> "META-INF/smack.providers"
}
