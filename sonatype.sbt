import xerial.sbt.Sonatype.GitHubHosting

licenses := Seq(
  "APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
)
publishMavenStyle := true
sonatypeProjectHosting := Some(
  GitHubHosting("gjsduarte", "sbt-datadog", "gjsduarte@gmail.com")
)