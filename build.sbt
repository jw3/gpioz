enablePlugins(GitVersioning)

organization := "com.github.jw3"
name := "gpioz"
git.useGitDescribe := true

scalaVersion := "2.12.6"
resolvers += Resolver.bintrayRepo("jw3", "maven")

libraryDependencies ++= {
  Seq(
    "com.github.jw3" % "pigpio" % "67.0-1.4.1",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "org.scalaz" %% "scalaz-zio" % "0.2.7",
    "org.scalaz" %% "testz-stdlib" % "0.0.5" % Test
  )
}
