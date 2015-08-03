import sbt._
import V._

object Libraries {

  lazy val specs2Core = "org.specs2" %% "specs2-core" % specs2

  lazy val specs2Mock = "org.specs2" %% "specs2-mock" % specs2

  lazy val sqliteJdbc = "org.xerial" % "sqlite-jdbc" % xerialJdbc

  lazy val android = "com.google.android" % "android" % androidJar

}