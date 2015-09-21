scalaVersion := "2.11.7"

javacOptions ++= Seq("-target", "1.7", "-source", "1.7")

platformTarget in Android := "android-22"

libraryDependencies ++= Seq(
  "com.fortysevendeg" %% "mvessel-android" % "0.1-SNAPSHOT",
  "com.android.support" % "recyclerview-v7" % "23.0.1",
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.play" %% "play-json" % "2.4.0-M2")

apkbuildExcludes in Android ++= Seq(
  "META-INF/LICENSE.txt",
  "META-INF/LICENSE",
  "META-INF/NOTICE.txt",
  "META-INF/NOTICE")

proguardScala in Android := true

proguardOptions in Android ++= Seq(
  "-ignorewarnings",
  "-keep class scala.Dynamic",
  "-keep class scala.concurrent.ExecutionContext",
  "-keep class com.fortysevendeg.mvessel.AndroidDriver",
  "-keep class slick.driver.SQLiteDriver"
)
