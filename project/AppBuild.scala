import sbt.Keys._
import sbt._
import sbtbuildinfo.BuildInfoPlugin
import Settings._

object AppBuild extends Build {

  lazy val root = (project in file("."))
    .settings(basicSettings: _*)
    .aggregate(core, util)

  lazy val core = (project in file("core"))
    .enablePlugins(BuildInfoPlugin)
    .configs(IntegrationTest)
    .settings(coreSettings: _*)
    .settings(Defaults.itSettings : _*)
    .settings(libraryDependencies ++= coreLibraries)
    .dependsOn(
      util,
      mockAndroid % "test->test;it->test")

  lazy val util = (project in file("util"))
    .settings(utilSettings: _*)
    .settings(libraryDependencies ++= utilLibraries)
    .dependsOn(mockAndroid % "test->test")

  lazy val mockAndroid = (project in file("mock-android"))
    .settings(mockAndroidSettings: _*)
    .settings(libraryDependencies ++= mockAndroidLibraries)

}