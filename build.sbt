name         := "sbt-datadog"
scalaVersion := "2.12.17"
sbtPlugin    := true
ThisBuild / pushRemoteCacheTo := Some(
  MavenCache("local-cache", baseDirectory.value / sys.env.getOrElse("CACHE_PATH", "sbt-cache"))
)
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.15" % "provided")
