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

import java.net.URL

import microsites.MicrositeKeys._
import Libraries._
import sbt._
import sbt.Keys._
import sbtbuildinfo.BuildInfoPlugin.autoImport._

trait Settings {

  this: Build with SettingsPublish =>

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

  lazy val noPublishSettings = Seq(
    publish := (),
    publishLocal := (),
    publishArtifact := false)

  lazy val docsSettings = basicSettings ++ orgSettings ++ noPublishSettings ++ Seq(
    micrositeName := "mvessel",
    micrositeDescription := "Mvessel is a JDBC driver written in Scala",
    micrositeHomepage := "http://47deg.com",
    micrositeBaseUrl := "/mvessel",
    micrositeDataDirectory := (resourceDirectory in Compile).value / "microsite" / "data",
    micrositeGithubOwner := "47deg",
    micrositeGithubRepo := "mvessel",
    micrositePalette := Map(
      "brand-primary"     -> "#FFC107",
      "brand-secondary"   -> "#2C3358",
      "brand-tertiary"    -> "#212641",
      "gray-dark"         -> "#494A4F",
      "gray"              -> "#76767E",
      "gray-light"        -> "#E6E7EC",
      "gray-lighter"      -> "#F4F5F9",
      "white-color"       -> "#FFFFFF"))

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