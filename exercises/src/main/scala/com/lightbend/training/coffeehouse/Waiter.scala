package com.lightbend.training.coffeehouse

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object Waiter {
  // message protocol
  case class ServeCoffee(coffee: Coffee)  // received message
  case class CoffeeServed(coffee: Coffee) // message to reply with

  def props(coffeeHouse: ActorRef): Props = Props(new Waiter(coffeeHouse))
}

class Waiter(coffeeHouse: ActorRef) extends Actor with ActorLogging {
  import Waiter._

  override def receive: Receive = {
    case ServeCoffee(coffee) =>
      // send a prepare coffee to the Barista before serving
      coffeeHouse ! CoffeeHouse.ApproveCoffee(coffee, sender())
    case Barista.CoffeePrepared(coffee, guest) =>
      guest ! CoffeeServed(coffee)
  }
}
