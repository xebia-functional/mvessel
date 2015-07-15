resolvers += Classpaths.sbtPluginReleases

resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")

addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.0.0")

addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.1.0")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.4.0")