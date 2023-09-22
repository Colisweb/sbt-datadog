name         := "sbt-datadog"
scalaVersion := "2.13.12"
sbtPlugin    := true
inThisBuild(PublishSettings.localCacheSettings)
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.16" % "provided")
