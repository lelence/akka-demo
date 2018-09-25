name := "actor-ws"

version := "0.0.1"

organization := "toan"

// resolvers += "Spray" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.14",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.14",
  "com.typesafe.akka" %% "akka-http" % "10.1.4",
  "org.java-websocket" % "Java-WebSocket" % "1.3.9",
  "javax.websocket" % "javax.websocket-api" % "1.1",
  "com.github.andyglow" %% "websocket-scala-client" % "0.2.4",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.14" % Test
)
