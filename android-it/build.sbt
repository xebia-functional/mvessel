android.Plugin.androidBuild

javacOptions ++= Seq("-target", "1.7", "-source", "1.7")

scalaVersion := "2.11.11"

platformTarget in Android := "android-23"

debugIncludesTests in Android := true

instrumentTestRunner in Android :=
  "android.support.test.runner.AndroidJUnitRunner"

libraryDependencies ++= Seq(
  "com.android.support.test" % "runner" % "0.4.1",
  "com.android.support.test" % "rules" % "0.4.1",
  "com.fortysevendeg" %% "mvessel-android" % "0.1" )

packagingOptions in Android := PackagingOptions(excludes = Seq(
  "LICENSE.txt",
  "META-INF/LICENSE",
  "META-INF/LICENSE.txt",
  "META-INF/NOTICE",
  "META-INF/NOTICE.txt"))

autoScalaLibrary := false

proguardScala in Android := true

proguardOptions in Android ++= Seq(
  "-ignorewarnings",
  "-keep class scala.Dynamic",
  "-keep class com.android.** { *; }",
  "-keep class com.fortysevendeg.** { *; }",
  "-keep public class * extends junit.framework.TestCase",
  "-keepclassmembers class * extends junit.framework.TestCase { *; }"
)
