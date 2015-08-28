import sbt.Keys._
import sbt._
import sbtbuildinfo.BuildInfoPlugin
import Settings._

object AppBuild extends Build {

  lazy val root = (project in file("."))
    .settings(basicSettings: _*)
    .aggregate(androidDriver, core)

  lazy val androidDriver = (project in file("android-driver"))
    .enablePlugins(BuildInfoPlugin)
    .configs(IntegrationTest)
    .settings(androidDriverSettings: _*)
    .settings(Defaults.itSettings : _*)
    .settings(libraryDependencies ++= androidDriverLibraries)
    .dependsOn(core, mockAndroid % "test->test;it->test")

  lazy val core = (project in file("core"))
    .enablePlugins(BuildInfoPlugin)
    .settings(coreSettings: _*)
    .settings(libraryDependencies ++= coreLibraries)

  lazy val mockAndroid = (project in file("mock-android"))
    .settings(mockAndroidSettings: _*)
    .settings(libraryDependencies ++= mockAndroidLibraries)

}