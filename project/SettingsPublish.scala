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

import com.typesafe.sbt.pgp.PgpKeys._
import sbt.Keys._
import sbt._

trait SettingsPublish {

  lazy val publishSnapshot = taskKey[Unit]("Publish only if the version is a SNAPSHOT")

  lazy val publishSettings = Seq(
    publishSnapshot := { if(isSnapshot.value) publishSigned.value },
    publishMavenStyle := true,
    // https://github.com/sbt/sbt-pgp/issues/80
    com.typesafe.sbt.SbtPgp.autoImport.pgpPassphrase := Some(sys.env("GPG_PASSPHRASE").toCharArray),
    com.typesafe.sbt.SbtPgp.autoImport.pgpPublicRing := file("local.pubring.asc"),
    com.typesafe.sbt.SbtPgp.autoImport.pgpSecretRing := file("local.secring.asc"),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    credentials += Credentials("Sonatype Nexus Repository Manager",
      "oss.sonatype.org",
      sys.env("CI_DEPLOY_USERNAME"),
      sys.env("CI_DEPLOY_PASSWORD")),
    publishArtifact in Test := false,
    homepage := Some(url("https://github.com/47deg/mvessel")),
    licenses := Seq("The MIT License (MIT)" -> url("https://opensource.org/licenses/MIT")),
    pomExtra :=
      <scm>
        <url>git@github.com:47deg/mvessel.git</url>
        <connection>scm:git:git@github.com:47deg/mvessel.git</connection>
      </scm>
      <developers>
        <developer>
          <id>47deg</id>
          <name>47 Degrees</name>
          <url>http://47deg.com</url>
        </developer>
      </developers>
  )

 }