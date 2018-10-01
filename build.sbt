lazy val gpioz =
  project
    .in(file("."))
    .aggregate(core, core)
    .settings(commonSettings: _*)
    .enablePlugins(GitVersioning)

lazy val core =
  project
    .in(file("core"))
    .settings(commonSettings: _*)
    .settings(name := "gpioz-core")
    .enablePlugins(GitVersioning)

lazy val examples =
  project
    .in(file("examples"))
    .dependsOn(core)
    .settings(commonSettings: _*)
    .settings(name := "gpioz-examples")
    .enablePlugins(GitVersioning)

lazy val commonSettings = Seq(
  organization := "com.github.jw3",
  name := "gpioz",
  git.useGitDescribe := true,
  scalaVersion := "2.12.6",
  resolvers += Resolver.bintrayRepo("jw3", "maven"),
  libraryDependencies ++= {
    lazy val scalatestVersion = "3.0.3"

    Seq(
      "com.github.jw3" % "pigpio" % "67.0-1.4.1",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
      "org.scalaz" %% "scalaz-zio" % "0.2.7",
      "org.scalactic" %% "scalactic" % scalatestVersion % Test,
      "org.scalatest" %% "scalatest" % scalatestVersion % Test,
    )
  }
)
