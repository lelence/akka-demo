
name := "actor-demo1"

version := "0.0.1"

organization := "toan"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.5.14",
	"com.typesafe.akka" %% "akka-cluster" % "2.5.14",
	"com.typesafe.akka" %% "akka-testkit" % "2.5.14" % Test
)