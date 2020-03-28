package com.lightbend.training.coffeehouse

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.lightbend.training.coffeehouse.Guest.CoffeeFinished

object Guest {
  case object CoffeeFinished

  def props(waiter: ActorRef, favouriteCoffee: Coffee): Props =
    Props(new Guest(waiter, favouriteCoffee))
}

// Waiter would be of type waiter but because Actors are protected we must pass the ActorRef
class Guest(waiter: ActorRef, favouriteCoffee: Coffee) extends Actor with ActorLogging {
  private var coffeeCount: Int = 0 // best practice to make vars in Actors private
  // even though the Actor cannot be accessed !!!

  override def receive: Receive = {
    case Waiter.CoffeeServed(coffee) =>
      coffeeCount += 1
      log.info(s"Enjoying my $coffeeCount yummy $coffee")
    case CoffeeFinished =>
      waiter ! Waiter.ServeCoffee(favouriteCoffee)
  }
}
