enablePlugins(JavaServerAppPackaging)

name := "BasicAkkaHTTP"

version := "1.0"

organization := "com.aj"

scalaVersion := "2.12.2"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  Resolver.bintrayRepo("hseeberger", "maven"))

libraryDependencies ++= {
  val AkkaVersion = "2.4.18"
  val AkkaHttpVersion = "10.0.6"
  val Json4sVersion = "3.5.2"
  Seq(
    "com.typesafe.akka" %% "akka-slf4j"      % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.json4s"        %% "json4s-native"   % Json4sVersion,
    "org.json4s"        %% "json4s-ext"      % Json4sVersion,
    "de.heikoseeberger" %% "akka-http-json4s" % "1.16.0",
    "de.heikoseeberger" %% "akka-http-jackson" % "1.18.0",
    "org.mongodb.scala" %% "mongo-scala-driver" % "2.2.1",

    //test libraries
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.pegdown" % "pegdown" % "1.6.0" % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion % "test",
    "org.mongodb" % "mongo-java-driver" % "3.4.0" % "test",
    "com.github.fakemongo" % "fongo" % "2.1.0" % "test"

  )
}

// Assembly settings
mainClass in Global := Some("com.aj.basicakkahttp.Main")

assemblyJarName in assembly := "BasicAkkaHTTP.jar"

testOptions in Test ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports"),
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
)
