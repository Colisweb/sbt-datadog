name          := "sbt-datadog"
versionScheme := Some("semver-spec")
scalaVersion  := "2.12.16"
sbtPlugin     := true

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.10" % "provided")

inThisBuild(
  List(
    organization := "com.guizmaii",
    homepage     := Some(url("https://github.com/guizmaii/sbt-datadog")),
    licenses     := Seq("Apache-2.0" -> url("http://opensource.org/licenses/https://opensource.org/licenses/Apache-2.0")),
    developers   := List(
      Developer(
        "guizmaii",
        "Jules Ivanic",
        "jules.ivanic@gmail.com",
        url("https://blog.jules-ivanic.com/#/"),
      )
    ),
  )
)
