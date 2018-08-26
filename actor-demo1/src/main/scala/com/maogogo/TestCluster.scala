package com.maogogo

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory

object TestCluster extends App {

  val config = ConfigFactory.load()

  implicit val system = ActorSystem("ClusterSystem", config)
  val worker = system.actorOf(Props[Worker], "Worker")
  val simpleClusterListener = system.actorOf(Props[SimpleClusterListener], "SimpleClusterListener")

}

class SimpleClusterListener extends Actor with ActorLogging {
  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case MemberUp(member) ⇒
      log.info("Member is Up: {}", member.address)
    case UnreachableMember(member) ⇒
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) ⇒
      log.info(
        "Member is Removed: {} after {}",
        member.address, previousStatus)
    case _: MemberEvent ⇒ 
      println("wpwepwpef--->>>")
      // ignore
  }
}