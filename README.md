#### This project is a fork of [Colisweb/sbt-datadog](https://github.com/Colisweb/sbt-datadog) with support for *sbt 0.13*

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.gjsduarte/sbt-datadog_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.gjsduarte/sbt-datadog_2.11)

# sbt-datadog

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
addSbtPlugin("com.github.gjsduarte" % "sbt-datadog" % "0.2.0")
```

To enable the Datadog APM for your project, add the `DatadogAPM` auto-plugin to your project.

```scala
enablePlugins(DatadogAPM)
```

Configuration
-------------

#### `apmVersion`

To use a specific Datadog Java APM Agent version, add the following to your `build.sbt` file:

```scala
datadog.apmVersion := "0.10.0"
```

#### `serviceName`

By default, the agent will use the sbt project `name` value as `service.name`. 

To use another value, add the following to your `build.sbt` file:

```scala
datadog.serviceName := "another name"
```

You can use your **host** (where you code run) enviroment variables in the value:  

```scala
datadog.serviceName := "another name ${MY_HOST_ENV_VAR}"
```

#### `agentHost`

By default, the agent `agent.host` value is `localhost`.

To use another value, add the following to your `build.sbt` file:

```scala
datadog.agentHost := "127.0.0.1"
```

You can use your **host** (where you code run) enviroment variables in the value:  

```scala
datadog.serviceName := "${MY_DD_HOST_IP}"
```

#### `agentPort`

By default, the agent `agent.port` value is `8126`.

To use another value, add the following to your `build.sbt` file:

```scala
datadog.agentPort := 9999
```

You can use your **host** (where you code run) enviroment variables in the value:  

```scala
datadog.agentPort := "${MY_DD_PORT}"
```

#### `environment`

By default, the `env` is not set.

To set the `env`, add the following to your `build.sbt` file:

```scala
datadog.environment := "staging"
```

You can use your **host** (where you code run) enviroment variables in the value:  

```scala
datadog.environment := "${MY_ENV}"
```

#### `enableNetty`

Netty Http Server and Client Instrumentation. Default value is `false`.

To use another value, add the following to your `build.sbt` file:

```scala
datadog.enableNetty := true
```

#### `enableAkkaHttp`

Akka-Http Server and Lagom Framework Instrumentation. Default value is `false`.

To use another value, add the following to your `build.sbt` file:

```scala
datadog.enableAkkaHttp := true
```

#### `enableDebug`

To return debug level application logs, enable debug mode with this flag. Default value is `false`.

To use another value, add the following to your `build.sbt` file:

```scala
datadog.enableDebug := true
```


#### Other possible settings

For more configuration option, look at the Datadog Java APM agent documentation: https://docs.datadoghq.com/tracing/setup/java/