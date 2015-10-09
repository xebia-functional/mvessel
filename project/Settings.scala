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

import sbt._
import sbt.Keys._
import Libraries._
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