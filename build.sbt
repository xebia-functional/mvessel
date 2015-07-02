import sbt.Keys._

scalaVersion := "2.11.6"

organization := "com.fortysevendeg"

organizationName := "47 Degrees"

organizationHomepage := Some(new URL("http://47deg.com"))

name := "scala-sqlite-droid"

version := "0.1-SNAPSHOT"

conflictWarning := ConflictWarning.disable

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

javaOptions in Test ++= Seq("-XX:MaxPermSize=128m", "-Xms512m", "-Xmx512m")

sbt.Keys.fork in Test := true

publishMavenStyle := true

publishArtifact in(Test, packageSrc) := true

logLevel := Level.Info

resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.defaultLocal,
  Classpaths.typesafeReleases,
  DefaultMavenRepository,
  Resolver.typesafeIvyRepo("snapshots"),
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.3.1" % "test",
  "org.specs2" %% "specs2-mock" % "3.3.1" % "test",
  "com.google.android" % "android" % "4.1.1.4" % "provided")

lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.fortysevendeg.android.sqlite"
  )