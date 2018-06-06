package com.colisweb.sbt

import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.archetypes.scripts.BashStartScriptPlugin.autoImport._
import com.typesafe.sbt.packager.archetypes.scripts.{BashStartScriptPlugin, BatStartScriptPlugin}
import sbt.Keys._
import sbt._
import sbt.librarymanagement.DependencyFilter

/**
  * Eagerly inspired by https://github.com/gilt/sbt-newrelic
  */
object DatadogAPM extends AutoPlugin {

  object autoImport {
    lazy val datadogVersion = settingKey[String]("Datadog agent version")
    lazy val datagodAgent   = taskKey[File]("Datagod agent jar location")
  }

  import autoImport._

  override def requires = BashStartScriptPlugin && BatStartScriptPlugin

  val DatadogConfig = config("dd-java-agent").hide

  override lazy val projectSettings = Seq(
    ivyConfigurations += DatadogConfig,
    datadogVersion := "0.9.0",
    libraryDependencies += "com.datadoghq" % "dd-java-agent" % datadogVersion.value % DatadogConfig,
    datagodAgent := findDatadogAgent(update.value),
    mappings in Universal += datagodAgent.value -> "datadog/dd-java-agent.jar",
    bashScriptExtraDefines += """addJava "-javaagent:${app_home}/../datadog/dd-java-agent.jar""""
  )

  private[this] val datadogFilter: DependencyFilter =
    configurationFilter("dd-java-agent") && artifactFilter(`type` = "jar")

  private[this] def findDatadogAgent(report: UpdateReport) = report.matching(datadogFilter).head

}
