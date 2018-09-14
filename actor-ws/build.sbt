
name := "actor-ws"

version := "0.0.1"

organization := "toan"

// resolvers += "Spray" at "http://repo.spray.io"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.5.14",
	"com.typesafe.akka" %% "akka-cluster" % "2.5.14",
	"com.typesafe.akka" %% "akka-http" % "10.1.4",
	"org.java-websocket" % "Java-WebSocket" % "1.3.9",
	"com.typesafe.akka" %% "akka-testkit" % "2.5.14" % Test
)