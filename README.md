# sbt-datadog

## Disclaimer

⚠️ **This is a fork of the original (that I also wrote a long time ago) [Colisweb/sbt-datadog](https://github.com/Colisweb/sbt-datadog)** ⚠️

# Documentation

This project is highly inspired by the fantastic [sbt-newrelic](https://github.com/gilt/sbt-newrelic) project.

We want to thanks [Gilt](http://tech.gilt.com) for their work on `sbt-newrelic` that allowed us to create this project really quickly.

Prerequisites
-------------

The plugin assumes that sbt-native-packager has been included in your SBT build configuration.
This can be done by adding the plugin following instructions at http://www.scala-sbt.org/sbt-native-packager/ or by adding
another plugin that includes and initializes it (e.g. the SBT plugin for Play 2.6.x).


Installation
------------

Add the following to your `project/plugins.sbt` file:

```scala
addSbtPlugin("com.guizmaii" % "sbt-datadog" % "4.3.0")
```

To enable the Datadog APM for your project, add the `DatadogAPM` auto-plugin to your project.

```scala
enablePlugins(DatadogAPM)
```

Configuration
-------------

#### `datadogApmVersion`

To use a specific Datadog Java APM Agent version, add the following to your `build.sbt` file:

```scala
datadogApmVersion := "0.98.1"
```

#### `datadogApmEnabled`

You can easily disable the Datadog APM by settings this setting to `false`.
Note that if you have the `DD_TRACE_ENABLED` environment variable set, it'll take precedence over this setting.
See https://docs.datadoghq.com/tracing/trace_collection/library_config/java/

Default value: `true`

```scala
datadogApmEnabled := true
```

#### `datadogServiceName`

By default, the agent will use the sbt project `name` value as `service.name`. 

To use another value, add the following to your `build.sbt` file:

```scala
datadogServiceName := "another name"
```

You can use your **host** (where you code run) environment variables in the value:  

```scala
datadogServiceName := "another name ${MY_HOST_ENV_VAR}"
```

#### `datadogAgentTraceUrl`

Defines how the APM will communicate with the Datadog Agent.

Two ways are available:
  - via Unix Socket (default)
  - via HTTP

By default, the agent `trace.agent.url` value is `/var/run/datadog/apm.socket`.

To use a different Unix Socket, add the following to your `build.sbt` file:

```scala
datadogAgentTraceUrl := TraceAgentUnixSocketUrl(socket = "/my/directory/my.socket")
```

To use the default HTTP URL (`locahost:8126`), add the following to your `build.sbt` file:

```scala
datadogAgentTraceUrl := TraceAgentUrl.defaultHttpUrl
```

To use a custom HTTP URL, add the following to your `build.sbt` file:

```scala
datadogAgentTraceUrl := TraceAgentHttpUrl(host = "my.host.address.com", port = "8888")
```


You can use your **host** (where you code run) environment variables in the values:  

```scala
datadogAgentTraceUrl := TraceAgentHttpUrl(host = "${MY_DD_HOST_IP}", port = "8888")
// Or
datadogAgentTraceUrl := TraceAgentUnixSocketUrl(socket = "${MY_DD_UNIX_SOCKET}")
```

#### `datadogEnableDebug`

To return debug level application logs, enable debug mode with this flag. Default value is `false`.

To use another value, add the following to your `build.sbt` file:

```scala
datadogEnableDebug := true
```

#### `datadogGlobalTags`

A list of default tags to be added to every span and every JMX metric. Default value is an empty list.

To add global tags:

```scala
datadogGlobalTags := Map("env" -> "testing", "version" -> "testing-4c84587e")
```

#### Other possible settings

For more configuration option, look at the Datadog Java APM agent documentation: https://docs.datadoghq.com/tracing/setup/java/