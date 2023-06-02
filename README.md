# sbt-datadog

[ ![Download](https://api.bintray.com/packages/colisweb/sbt-plugins/sbt-datadog/images/download.svg) ](https://bintray.com/colisweb/sbt-plugins/sbt-datadog/_latestVersion)

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

addSbtPlugin("com.colisweb" % "sbt-datadog" % "2.1.0")
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
datadogApmVersion := "0.74.1"
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

#### `datadogAgentHost`

By default, the agent `agent.host` value is `localhost`.

To use another value, add the following to your `build.sbt` file:

```scala
datadogAgentHost := "127.0.0.1"
```

You can use a **host** environment variables in the value:  

```scala
datadogAgentHost := "${MY_DD_HOST_IP}"
```

This variable will be evaluated in the context of your running code!


#### `datadogAgentPort`

By default, the agent `agent.port` value is `"8126"`.

To use another value, add the following to your `build.sbt` file:

```scala
datadogAgentPort := "9999"
```

You can use a **port** environment variable in the value:  

```scala
datadogAgentPort := "${MY_DD_PORT}"
```

This variable will be evaluated in the context of your running code!

#### `datadogEnableNetty`

Netty Http Server and Client Instrumentation. Default value is `false`.

To use another value, add the following to your `build.sbt` file:

```scala
datadogEnableNetty := true
```

#### `datadogEnableAkkaHttp`

Akka-Http Server and Lagom Framework Instrumentation. Default value is `false`.

To use another value, add the following to your `build.sbt` file:

```scala
datadogEnableAkkaHttp := true
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
