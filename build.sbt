offline := true

crossScalaVersions := Seq(
  /*"2.11.7", 多余，不需要两个*/
  "2.11.11"
  /*"2.12.2", 有一些编译问题：`the interface is not a direct parent`。*/
  /*"2.12.6" jitpack 不会cross-build, 还是保留2.11*/)

// 解决生成文档报错导致 jitpack.io 出错的问题。
publishArtifact in packageDoc := false

resolvers += "google" at "https://maven.google.com"

libraryDependencies ++= Seq(
  // 解决 SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
  // 以便 slf4j 日志系统能够兼容 Android Logcat.
  // https://stackoverflow.com/questions/34998237/slf4j-android-1-7-not-logging-on-logcat?noredirect=1
  "org.slf4j" % "slf4j-android" % "[1.7.14,)",
  "org.slf4j" % "slf4j-api" % "[1.7.0,)"
)

publish := ()
publishM2 := ()
publishLocal := ()
publishArtifact := false
