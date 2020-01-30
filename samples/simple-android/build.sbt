scalaVersion := "2.12.6"

javacOptions ++= Seq("-target", "1.7", "-source", "1.7")

platformTarget in Android := "android-22"

libraryDependencies ++= Seq(
  "com.fortysevendeg" %% "mvessel-android" % "0.1-SNAPSHOT" )

apkbuildExcludes in Android += "LICENSE.txt"

proguardScala in Android := true

proguardOptions in Android ++= Seq(
  "-ignorewarnings",
  "-keep class scala.Dynamic"
)

publish := ()
publishM2 := ()
publishLocal := ()
publishArtifact := false
