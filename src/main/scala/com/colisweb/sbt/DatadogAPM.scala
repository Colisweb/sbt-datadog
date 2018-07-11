package com.colisweb.sbt

import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.archetypes.scripts.BashStartScriptPlugin.autoImport.bashScriptExtraDefines
import com.typesafe.sbt.packager.archetypes.scripts.{BashStartScriptPlugin, BatStartScriptPlugin}
import sbt.Keys._
import sbt.librarymanagement.DependencyFilter
import sbt.{Def, _}

/**
  * Eagerly inspired by https://github.com/gilt/sbt-newrelic
  */
object DatadogAPM extends AutoPlugin {

  object autoImport {
    lazy val datadogVersion   = settingKey[String]("Datadog agent version")
    lazy val datagodJavaAgent = taskKey[File]("Datagod agent jar location")
    lazy val datadogServiceName = taskKey[String](
      "The name of a set of processes that do the same job. Used for grouping stats for your application. Default value is the sbt project name")
    lazy val datadogAgentHost = taskKey[String](
      """Hostname for where to send traces to. If using a containerized environment, configure this to be the host ip. See our docker docs for additional detail. Default value: "localhost"""")
    lazy val datadogEnv = taskKey[String]("Environment. https://docs.datadoghq.com/tracing/setup/first_class_dimensions/. By default, this settings is not set")
    lazy val datadogEnableNetty = taskKey[Boolean]("Netty Http Server and Client Instrumentation. Default value: false")
    lazy val datadogEnableAkkaHttp =
      taskKey[Boolean]("Akka-Http Server and Lagom Framework Instrumentation. Default value: false")
    lazy val datadogEnableDebug = taskKey[Boolean]("To return debug level application logs, enable debug mode. Default value: false")
  }
  import autoImport._

  override def requires = BashStartScriptPlugin && BatStartScriptPlugin

  val DatadogConfig = config("dd-java-agent").hide

  override lazy val projectSettings = Seq(
    ivyConfigurations += DatadogConfig,
    datadogVersion := "0.10.0",
    datagodJavaAgent := findDatadogJavaAgent(update.value),
    datadogServiceName := name.value,
    datadogAgentHost := "localhost",
    datadogEnv := "",
    datadogEnableNetty := false,
    datadogEnableAkkaHttp := false,
    datadogEnableDebug := false,
    libraryDependencies += "com.datadoghq"          % "dd-java-agent" % datadogVersion.value % DatadogConfig,
    mappings in Universal += datagodJavaAgent.value -> "datadog/dd-java-agent.jar",
    bashScriptExtraDefines += """addJava "-javaagent:${app_home}/../datadog/dd-java-agent.jar"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.service.name=${datadogServiceName.value}"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.agent.host=${datadogAgentHost.value}"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.integration.netty.enabled=${datadogEnableNetty.value}"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.integration.akka-http.enabled=${datadogEnableAkkaHttp.value}"""",
    bashScriptExtraDefines += (if (datadogEnv.value.nonEmpty) s"""addJava "-Ddd.trace.span.tags="env:${datadogEnv.value}""""" else """echo "Datadog env is not set"""") ,
    bashScriptExtraDefines += (if (datadogEnableDebug.value) s"""addJava "-Ddatadog.slf4j.simpleLogger.defaultLogLevel=debug"""" else """echo "Datadog debug mode disabled"""")
  )

  private[this] def findDatadogJavaAgent(report: UpdateReport) = report.matching(datadogFilter).head

  private[this] val datadogFilter: DependencyFilter =
    configurationFilter("dd-java-agent") && artifactFilter(`type` = "jar")

}
