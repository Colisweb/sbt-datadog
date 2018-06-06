# sbt-datadog

[ ![Download](https://api.bintray.com/packages/colisweb/sbt-plugins/sbt-datadog/images/download.svg) ](https://bintray.com/colisweb/sbt-plugins/sbt-datadog/_latestVersion)

This project is highly inspired by (actually, copied from) the fantastic [sbt-newrelic](https://github.com/gilt/sbt-newrelic) project.

We want to thanks [Gilt](http://tech.gilt.com) for their work on `sbt-newrelic` that permits us to create this project really quickly.

Prerequisites
-------------

The plugin assumes that sbt-native-packager has been included in your SBT build configuration.
This can be done by adding the plugin following instructions at http://www.scala-sbt.org/sbt-native-packager/ or by adding
another plugin that includes and initializes it (e.g. the SBT plugin for Play 2.6.x).


Installation
------------

Add the following to your `project/plugins.sbt` file:

```scala
resolvers += Resolver.bintrayRepo("colisweb", "sbt-plugins")

addSbtPlugin("com.colisweb.sbt" % "sbt-datadog" % "0.1.5")
```

To enable the Datadog APM for your project, add the `DatadogAPM` auto-plugin to your project.

```scala
enablePlugins(DatadogAPM)
```

Configuration
-------------

#### `datadogVersion`

To use a specific Datadog Java APM Agent version, add the following to your `build.sbt` file:

```scala
datadogVersion := "0.9.0"
```

#### `datadogServiceName`

By default, the agent will use the sbt project `name` value as `service.name`. 

To use another value, add the following to your `build.sbt` file:

```scala
datadogServiceName := "another name"
```

You can use your **host** (where you code run) enviroment variables in the value:  

```scala
datadogServiceName := "another name ${MY_HOST_ENV_VAR}"
```

#### `datadogAgentHost`

By default, the agent `agent.host` value is `localhost`.

To use another value, add the following to your `build.sbt` file:

```scala
datadogAgentHost := "127.0.0.1"
```

You can use your **host** (where you code run) enviroment variables in the value:  

```scala
datadogServiceName := "${MY_DD_HOST_IP}"
```

#### Other possible settings

For more configuration option, look at the Datadog Java APM agent documentation: https://docs.datadoghq.com/tracing/setup/java/