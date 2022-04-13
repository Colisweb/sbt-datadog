package com.guizmaii.sbt

import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.archetypes.scripts.BashStartScriptPlugin.autoImport.bashScriptExtraDefines
import com.typesafe.sbt.packager.archetypes.scripts.{BashStartScriptPlugin, BatStartScriptPlugin}
import sbt._
import sbt.Keys._
import sbt.librarymanagement.DependencyFilter

/**
 * Eagerly inspired by https://github.com/gilt/sbt-newrelic
 */
object DatadogAPM extends AutoPlugin {

  /**
   * See:
   *  - https://docs.datadoghq.com/tracing/setup_overview/setup/java/?tab=containers
   *  - https://docs.datadoghq.com/agent/kubernetes/apm/?tab=ipport#configure-your-application-pods-in-order-to-communicate-with-the-datadog-agent
   */
  sealed trait TraceAgentUrl
  object TraceAgentUrl {
    final case class TraceAgentHttpUrl(host: String, port: String) extends TraceAgentUrl
    final case class TraceAgentUnixSocketUrl(socket: String)       extends TraceAgentUrl

    final val defaultHttpUrl: TraceAgentUrl       = TraceAgentHttpUrl(host = "localhost", port = "8126")
    final val defaultUnixSocketUrl: TraceAgentUrl = TraceAgentUnixSocketUrl(socket = "/var/run/datadog/apm.socket")
  }

  object autoImport {
    lazy val datadogApmVersion    = settingKey[String]("Datadog APM agent version")
    lazy val datadogJavaAgent     = taskKey[File]("Datadog agent jar location")
    lazy val datadogServiceName   = taskKey[String](
      "The name of a set of processes that do the same job. Used for grouping stats for your application. Default value is the sbt project name"
    )
    lazy val datadogAgentTraceUrl = taskKey[TraceAgentUrl](
      "Configures how the APM communicates with the Datadog agent. By default it uses the default Datadog Unix Socket `/var/run/datadog/apm.socket`. More information, see https://docs.datadoghq.com/agent/kubernetes/apm/?tab=ipport#configure-your-application-pods-in-order-to-communicate-with-the-datadog-agent"
    )
    lazy val datadogEnableDebug   =
      taskKey[Boolean]("To return debug level application logs, enable debug mode. Default value: false")
    lazy val datadogGlobalTags    = taskKey[Map[String, String]](
      "A list of default tags to be added to every span and every JMX metric. Default value: Empty List"
    )
  }
  import autoImport.*

  override def requires = BashStartScriptPlugin && BatStartScriptPlugin

  val DatadogConfig = config("dd-java-agent").hide

  override lazy val projectSettings = Seq(
    ivyConfigurations += DatadogConfig,
    datadogApmVersion                              := "0.98.1",
    datadogJavaAgent                               := findDatadogJavaAgent(update.value),
    datadogServiceName                             := name.value,
    datadogAgentTraceUrl                           := TraceAgentUrl.defaultUnixSocketUrl,
    datadogEnableDebug                             := false,
    datadogGlobalTags                              := Map.empty,
    libraryDependencies += "com.datadoghq"          % "dd-java-agent" % datadogApmVersion.value % DatadogConfig,
    Universal / mappings += datadogJavaAgent.value -> "datadog/dd-java-agent.jar",
    bashScriptExtraDefines += """addJava "-javaagent:${app_home}/../datadog/dd-java-agent.jar"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.service.name=${datadogServiceName.value}"""",
    bashScriptExtraDefines += {
      datadogAgentTraceUrl.value match {
        case TraceAgentUrl.TraceAgentHttpUrl(host, port)   => s"""addJava "-Ddd.trace.agent.url=http://$host:$port""""
        case TraceAgentUrl.TraceAgentUnixSocketUrl(socket) => s"""addJava "-Ddd.trace.agent.url=unix://$socket""""
      }
    },
    bashScriptExtraDefines += {
      val debugEnabled = datadogEnableDebug.value
      if (debugEnabled) s"""addJava "-Ddatadog.slf4j.simpleLogger.defaultLogLevel=debug""""
      else """echo "Datadog debug mode disabled""""
    },
    bashScriptExtraDefines += {
      val globalTags = datadogGlobalTags.value
      val tags       = globalTags.map { case (key, value) => s"$key:$value" }.mkString(",")
      s"""addJava -Ddd.trace.global.tags=$tags"""
    },
  )

  private[this] def findDatadogJavaAgent(report: UpdateReport) = report.matching(datadogFilter).head

  private[this] val datadogFilter: DependencyFilter =
    configurationFilter("dd-java-agent") && artifactFilter(`type` = "jar")

}
