package com.gjsduarte.sbt

import com.typesafe.sbt.SbtNativePackager._
import com.typesafe.sbt.packager.archetypes.scripts.BashStartScriptPlugin.autoImport.bashScriptExtraDefines
import com.typesafe.sbt.packager.archetypes.scripts.{BashStartScriptPlugin, BatStartScriptPlugin}
import sbt.Keys._
import sbt._

/**
  * Eagerly inspired by https://github.com/gilt/sbt-newrelic
  */
object DatadogAPM extends AutoPlugin {

  object autoImport {
    object datadog {
      lazy val apmVersion = settingKey[String]("Datadog APM agent version")
      lazy val javaAgent  = taskKey[File]("Datadog agent jar location")
      lazy val serviceName = taskKey[String](
        "The name of a set of processes that do the same job. Used for grouping stats for your application. Default value is the sbt project name")
      lazy val agentHost = taskKey[String](
        """Hostname for where to send traces to. If using a containerized environment, configure this to be the host ip. See our docker docs for additional detail. Default value: "localhost"""")
      lazy val agentPort = taskKey[Int](
        "Port number the Agent is listening on for configured host. Default value: 8126")
      lazy val environment = taskKey[String](
        "Environment. https://docs.datadoghq.com/tracing/setup/first_class_dimensions/. By default, this settings is not set")
      lazy val enableNetty = taskKey[Boolean]("Netty Http Server and Client Instrumentation. Default value: false")
      lazy val enableAkkaHttp =
        taskKey[Boolean]("Akka-Http Server and Lagom Framework Instrumentation. Default value: false")
      lazy val enableDebug =
        taskKey[Boolean]("To return debug level application logs, enable debug mode. Default value: false")
      lazy val traceAnnotations = taskKey[String](
        "List of class/interface and methods to trace. Similar to adding @Trace, but without changing code. https://docs.datadoghq.com/tracing/setup/first_class_dimensions/. Default value: datadog.trace.api.Trace")
      lazy val traceMethods = taskKey[String](
        "List of class/interface and methods to trace. Similar to adding @Trace, but without changing code. https://docs.datadoghq.com/tracing/setup/first_class_dimensions/. By default, this setting is not set")
    }
  }
  import autoImport._

  override def requires = BashStartScriptPlugin && BatStartScriptPlugin

  val DatadogConfig = config("dd-java-agent").hide

  override lazy val projectSettings = Seq(
    ivyConfigurations += DatadogConfig,

    // Defaults
    datadog.apmVersion := "0.16.0",
    datadog.javaAgent := findDatadogJavaAgent(update.value),
    datadog.serviceName := name.value,
    datadog.agentHost := "localhost",
    datadog.agentPort := 8126,
    datadog.environment := "",
    datadog.enableNetty := false,
    datadog.enableAkkaHttp := false,
    datadog.enableDebug := false,
    datadog.traceAnnotations := "datadog.trace.api.Trace",
    datadog.traceMethods := "",

    libraryDependencies += "com.datadoghq" % "dd-java-agent" % datadog.apmVersion.value % DatadogConfig,
    mappings in Universal += datadog.javaAgent.value -> "datadog/dd-java-agent.jar",

    // Bash arguments
    bashScriptExtraDefines += """addJava "-javaagent:${app_home}/../datadog/dd-java-agent.jar"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.service.name=${datadog.serviceName.value}"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.agent.host=${datadog.agentHost.value}"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.agent.port=${datadog.agentPort.value}"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.integration.netty.enabled=${datadog.enableNetty.value}"""",
    bashScriptExtraDefines += s"""addJava "-Ddd.integration.akka-http.enabled=${datadog.enableAkkaHttp.value}"""",
    bashScriptExtraDefines += {
      val environment = datadog.environment.value
      if (environment.nonEmpty) s"""addJava "-Ddd.trace.span.tags=env:$environment"""" else """echo "Datadog env is not set""""
    },
    bashScriptExtraDefines += {
      val debugEnabled = datadog.enableDebug.value
      if (debugEnabled) s"""addJava "-Ddatadog.slf4j.simpleLogger.defaultLogLevel=debug""""
      else """echo "Datadog debug mode disabled""""
    },
    bashScriptExtraDefines += s"""addJava "-Ddd.trace.annotations=${datadog.traceAnnotations.value}"""",
    bashScriptExtraDefines += {
      val traceMethods = datadog.traceMethods.value
      if (traceMethods.nonEmpty) s"""addJava "-Ddd.trace.methods=$traceMethods"""" else """echo "No trace methods defined""""
    }
  )

  private[this] def findDatadogJavaAgent(report: UpdateReport) = report.matching(datadogFilter).head

  private[this] val datadogFilter: DependencyFilter =
    configurationFilter("dd-java-agent") && artifactFilter(`type` = "jar")

}
