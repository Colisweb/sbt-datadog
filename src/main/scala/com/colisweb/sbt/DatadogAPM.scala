package com.colisweb.sbt

import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.archetypes.scripts.BashStartScriptPlugin.autoImport.bashScriptExtraDefines
import com.typesafe.sbt.packager.archetypes.scripts.{BashStartScriptPlugin, BatStartScriptPlugin}
import sbt.Keys._
import sbt._
import sbt.librarymanagement.DependencyFilter

/**
  * Eagerly inspired by https://github.com/gilt/sbt-newrelic
  */
object DatadogAPM extends AutoPlugin {

  object autoImport {
    lazy val datadogApmVersion = settingKey[String]("Datadog APM agent version")
    lazy val datadogJavaAgent  = taskKey[File]("Datadog agent jar location")
    lazy val datadogServiceName = taskKey[String](
      "The name of a set of processes that do the same job. Used for grouping stats for your application. Default value is the sbt project name"
    )
    lazy val datadogAgentHost = taskKey[String](
      """Hostname for where to send traces to. If using a containerized environment, configure this to be the host ip. See our docker docs for additional detail. Default value: "localhost""""
    )
    lazy val datadogAgentPort =
      taskKey[String]("Port number the Agent is listening on for configured host. Default value: 8126")
    lazy val datadogEnv = taskKey[String](
      "Environment. https://docs.datadoghq.com/tracing/setup/first_class_dimensions/. By default, this settings is not set"
    )
    lazy val datadogEnableNetty = taskKey[Boolean]("Netty Http Server and Client Instrumentation. Default value: false")
    lazy val datadogEnableAkkaHttp =
      taskKey[Boolean]("Akka-Http Server and Lagom Framework Instrumentation. Default value: false")
    lazy val datadogEnableDebug =
      taskKey[Boolean]("To return debug level application logs, enable debug mode. Default value: false")
    lazy val datadogGlobalTags = taskKey[Seq[(String, String)]](
      "A list of default tags to be added to every span and every JMX metric. Default value: Empty List"
    )
  }
  import autoImport._

  override def requires = BashStartScriptPlugin && BatStartScriptPlugin

  val DatadogConfig = config("dd-java-agent").hide

  override lazy val projectSettings = Seq(
    ivyConfigurations += DatadogConfig,
    datadogApmVersion := "0.46.0",
    datadogJavaAgent := findDatadogJavaAgent(update.value),
    datadogServiceName := name.value,
    datadogAgentHost := "localhost",
    datadogAgentPort := "8126",
    datadogEnv := "",
    datadogEnableNetty := false,
    datadogEnableAkkaHttp := false,
    datadogEnableDebug := false,
    datadogGlobalTags := Nil,
    libraryDependencies += "com.datadoghq"          % "dd-java-agent" % datadogApmVersion.value % DatadogConfig,
    mappings in Universal += datadogJavaAgent.value -> "datadog/dd-java-agent.jar",
    bashScriptExtraDefines += """addJava "-javaagent:${app_home}/../datadog/dd-java-agent.jar"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.service.name=${datadogServiceName.value}"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.agent.host=${datadogAgentHost.value}"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.agent.port=${datadogAgentPort.value}"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.integration.netty.enabled=${datadogEnableNetty.value}"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.integration.akka-http.enabled=${datadogEnableAkkaHttp.value}"""",
    bashScriptExtraDefines += {
      val env = datadogEnv.value
      if (env.nonEmpty) s"""addJava "-Ddd.trace.global.tags=env:$env"""" else """echo "Datadog env is not set""""
    },
    bashScriptExtraDefines += {
      val debugEnabled = datadogEnableDebug.value
      if (debugEnabled) s"""addJava "-Ddatadog.slf4j.simpleLogger.defaultLogLevel=debug""""
      else """echo "Datadog debug mode disabled""""
    },
    bashScriptExtraDefines ++= datadogGlobalTags.value.map {
      case (key, value) =>
        s"""addJava -Ddd.trace.global.tags=$key:$value"""
    }
  )

  private[this] def findDatadogJavaAgent(report: UpdateReport) = report.matching(datadogFilter).head

  private[this] val datadogFilter: DependencyFilter =
    configurationFilter("dd-java-agent") && artifactFilter(`type` = "jar")

}
