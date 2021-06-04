name := "sbt-datadog"
scalaVersion := "2.13.6"
sbtPlugin := true
ThisBuild / pushRemoteCacheTo := Some(
  MavenCache("local-cache", baseDirectory.value / sys.env.getOrElse("CACHE_PATH", "sbt-cache"))
)
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager"       % "1.8.1" % "provided")
