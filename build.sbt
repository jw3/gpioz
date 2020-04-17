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
    .settings(dockerSettings: _*)
    .settings(name := "gpioz-examples")
    .enablePlugins(GitVersioning, JavaServerAppPackaging, DockerPlugin)

lazy val commonSettings = Seq(
  organization := "com.github.jw3",
  name := "gpioz",
  git.useGitDescribe := true,
  scalaVersion := "2.12.6",
  resolvers += Resolver.bintrayRepo("jw3", "maven"),
  libraryDependencies ++= {
    lazy val scalatestVersion = "3.0.3"

    Seq(
      "dev.zio" %% "zio" % "1.0.0-RC18-2",
      "com.github.jw3" % "pigpio" % "67.0-1.4.1",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
      "org.scalactic" %% "scalactic" % scalatestVersion % Test,
      "org.scalatest" %% "scalatest" % scalatestVersion % Test,
    )
  }
)

lazy val dockerSettings = Seq(
  dockerBaseImage := sys.env.getOrElse("BASE_IMAGE", "openjdk:8"),
  dockerUpdateLatest := true
)
