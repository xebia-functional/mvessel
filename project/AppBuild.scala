/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2012 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 */

import Libraries._
import SettingsPublish._
import com.typesafe.sbt.SbtSite.SiteKeys._
import com.typesafe.sbt.SbtSite._
import sbt.Keys._
import sbt._
import sbtbuildinfo.BuildInfoPlugin
import sbtbuildinfo.BuildInfoPlugin.autoImport._
import sbtunidoc.Plugin.UnidocKeys._
import sbtunidoc.Plugin._
import tut.Plugin._

object AppBuild extends Build {

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

  lazy val androidDriverSettings = basicSettings ++ orgSettings ++ publishSettings ++ Seq(
    name := "mvessel-android",
    version := V.project,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.fortysevendeg.mvessel",
    fork in Test := true)

  lazy val coreSettings = basicSettings ++ orgSettings ++ publishSettings ++ Seq(
    name := "mvessel",
    version := V.project,
    fork in Test := true)

  lazy val mockAndroidSettings = basicSettings ++ orgSettings

  lazy val docsSettings = basicSettings ++
    orgSettings ++
    noPublishSettings ++
    unidocSettings ++
    site.settings ++
    site.jekyllSupport() ++
    tutSettings ++
    Seq(
      autoAPIMappings := true,
      unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(core, androidDriver),
      site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "api"),
      site.addMappingsToSiteDir(tut, "_tut"),
//      scalacOptions in (ScalaUnidoc, unidoc) ++= Seq(
//        "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
//        "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath,
//        "-diagrams"
//      ),
      tutScalacOptions ~= (_.filterNot(Set("-Ywarn-unused-import", "-Ywarn-dead-code"))),
      includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md")

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
    .settings(coreSettings: _*)
    .settings(libraryDependencies ++= coreLibraries)

  lazy val mockAndroid = (project in file("mock-android"))
    .settings(mockAndroidSettings: _*)
    .settings(libraryDependencies ++= mockAndroidLibraries)

  lazy val docs = (project in file("docs"))
    .settings(moduleName := "mvessel-docs")
    .settings(docsSettings)
    .dependsOn(androidDriver, core)

  lazy val noPublishSettings = Seq(
    publish := (),
    publishLocal := (),
    publishArtifact := false)

}