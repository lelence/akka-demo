
name := "actor-https"

version := "0.0.1"

organization := "toan"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.5.14",
	"com.typesafe.akka" %% "akka-http" % "10.1.4",
	"com.typesafe.akka" %% "akka-stream" % "2.5.14",
	"com.typesafe.akka" %% "akka-cluster" % "2.5.14",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.14" % Test
)