import java.net.URL

import sbt._
import sbt.Keys._
import Libraries._
import sbtbuildinfo.BuildInfoPlugin.autoImport._

object Settings {

  lazy val commonResolvers = Seq(
    Resolver.mavenLocal,
    Resolver.defaultLocal,
    Classpaths.typesafeReleases,
    DefaultMavenRepository,
    Resolver.typesafeIvyRepo("snapshots"),
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
  )

  lazy val basicSettings = Seq(
    scalaVersion := V.scala,
    resolvers ++= commonResolvers,
    logLevel := Level.Info,
    scalacOptions in Compile ++= Seq("-deprecation", "-unchecked", "-feature"),
    javacOptions in Compile ++= Seq("-target", V.java, "-source", V.java, "-deprecation"),
    javaOptions in Test ++= Seq("-XX:MaxPermSize=128m", "-Xms512m", "-Xmx512m")
  )

  lazy val orgSettings = Seq(
    organization := "com.fortysevendeg",
    organizationName := "47 Degrees",
    organizationHomepage := Some(new URL("http://47deg.com")))

  lazy val androidDriverSettings = basicSettings ++ orgSettings ++ Seq(
    name := "mvessel-android",
    version := V.project,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.fortysevendeg.mvessel",
    fork in Test := true)

  lazy val coreSettings = basicSettings ++ orgSettings ++ Seq(
    name := "mvessel",
    version := V.project,
    fork in Test := true)

  lazy val mockAndroidSettings = basicSettings ++ orgSettings

  lazy val androidDriverLibraries = Seq(
    specs2Core % "it,test",
    specs2Mock % "it,test",
    sqliteJdbc % "it",
    android % "provided")

  lazy val coreLibraries = Seq(
    specs2Core % "test",
    specs2Mock % "test")

  lazy val mockAndroidLibraries = Seq(
    android % "provided")

 }