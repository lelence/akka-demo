package com.maogogo

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

trait Message

case object StartMessage extends Message

case object StopMessage extends Message

object TestActor extends App {

  val system = ActorSystem("pingpong")
  val worker = system.actorOf(Props[Worker], "pinger")

  //  worker.
  worker ! StartMessage

}

class Worker extends Actor {

  override def preStart() = {
    println("hello")
    //    context.
  }

  def receive: Actor.Receive = {
    case StartMessage ⇒ println("start")
    case StopMessage ⇒
      println("stop")
      context.stop(self)
  }

}