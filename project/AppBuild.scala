import sbt.Keys._
import sbt._
import sbtbuildinfo.BuildInfoPlugin
import Settings._

object AppBuild extends Build {

  lazy val root = (project in file("."))
    .aggregate(core, android, mockAndroid)

  lazy val core = (project in file("core"))
    .enablePlugins(BuildInfoPlugin)
    .configs(IntegrationTest)
    .settings(coreSettings: _*)
    .settings(Defaults.itSettings : _*)
    .settings(libraryDependencies ++= coreLibraries)
    .dependsOn(mockAndroid % "test->test;it->test")

  lazy val android = (project in file("android"))
    .settings(androidSettings: _*)
    .settings(libraryDependencies ++= androidLibraries)

  lazy val mockAndroid = (project in file("mock-android"))
    .settings(mockAndroidSettings: _*)
    .settings(libraryDependencies ++= mockAndroidLibraries)

}