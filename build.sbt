enablePlugins(GitVersioning)

organization := "com.github.jw3"
name := "gpioz"
git.useGitDescribe := true

scalaVersion := "2.12.6"
resolvers += Resolver.bintrayRepo("jw3", "maven")

libraryDependencies ++= {
  Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
  )
}
