organization := "com.guizmaii"
name         := "sbt-datadog"
version      := sys.env.getOrElse("RELEASE_TAG", "0.0.1-SNAPSHOT")
scalaVersion := "2.12.15"
sbtPlugin    := true

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.9" % "provided")

val GITHUB_OWNER   = "guizmaii"
val GITHUB_PROJECT = "sbt-datadog"

ThisBuild / publishMavenStyle := true
ThisBuild / publishTo         := Some(
  s"GitHub $GITHUB_OWNER Apache Maven Packages of $GITHUB_PROJECT" at s"https://maven.pkg.github.com/$GITHUB_OWNER/$GITHUB_PROJECT"
)

ThisBuild / resolvers += s"GitHub $GITHUB_OWNER Apache Maven Packages" at s"https://maven.pkg.github.com/$GITHUB_OWNER/_/"
ThisBuild / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  GITHUB_OWNER,
  (env("GITHUB_TOKEN") orElse env("GH_PACKAGES_TOKEN")).getOrElse(
    throw new RuntimeException("Missing env variable: `GITHUB_TOKEN` or `GH_PACKAGES_TOKEN`")
  ),
)

def env(v: String): Option[String] = sys.env.get(v)
