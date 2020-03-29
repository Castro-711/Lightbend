package com.lightbend.training.coffeehouse

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext.Implicits.global

object Barista {
  // message protocol
  case class PrepareCoffee(coffee: Coffee, guest: ActorRef)
  case class CoffeePrepared(coffee: Coffee, guest: ActorRef)

  def props(prepareCoffeeDuration: FiniteDuration): Props = Props(new Barista(prepareCoffeeDuration))
}

class Barista(prepareCoffeeDuration: FiniteDuration) extends Actor with ActorLogging {
  import Barista._

  override def receive: Receive = {
    case PrepareCoffee(coffee, guest) =>
      busy(prepareCoffeeDuration)
      context.system.scheduler.scheduleOnce(prepareCoffeeDuration, guest, PrepareCoffee)
      sender() ! CoffeePrepared(coffee, guest)
  }
}
