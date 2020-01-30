addSbtPlugin("org.scala-android" % "sbt-android" % "[1.7.9,)")

resolvers += Classpaths.sbtPluginReleases
resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"

//addSbtPlugin("org.scoverage" % "sbt-scoverage" % "[1.0.4,)")

//addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "[1.1.0,)")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "[0.4.0,)")

//addSbtPlugin("com.jsuereth" % "sbt-pgp" % "[1.0.0,)")

addSbtPlugin("com.fortysevendeg"  % "sbt-microsites" % "[0.3.2,)")
