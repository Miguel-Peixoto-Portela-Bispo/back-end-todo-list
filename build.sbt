val scala3Version = "3.5.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "project",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test
  )

val PekkoVersion = "1.0.3"
val PekkoHttpVersion = "1.0.1"
libraryDependencies ++= Seq(
  "org.apache.pekko" %% "pekko-actor-typed" % PekkoVersion,
  "org.apache.pekko" %% "pekko-stream" % PekkoVersion,
  "org.apache.pekko" %% "pekko-http" % PekkoHttpVersion
)
libraryDependencies += "org.jooq" % "jooq" % "3.19.13"
libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.13.1"
libraryDependencies += "ch.epfl.scala" % "sbt-bloop_2.12_1.0" % "2.0.2"
cancelable in Global := true
run / fork := true
