package com.lightbend.training.coffeehouse

import akka.actor.{Actor, ActorLogging, Props}

object Waiter {
  // message protocol
  case class ServeCoffee(coffee: Coffee)  // received message
  case class CoffeeServed(coffee: Coffee) // message to reply with

  def props(): Props = Props(new Waiter)
}

class Waiter extends Actor with ActorLogging {
  import Waiter._

  override def receive: Receive = {
    case ServeCoffee(coffee) => sender() ! CoffeeServed(coffee)
  }
}
