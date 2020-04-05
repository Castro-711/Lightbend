package com.lightbend.training.coffeehouse

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Timers}

import scala.concurrent.duration.FiniteDuration

object Guest {
  case object CoffeeFinished
  case object CaffeineException extends IllegalStateException

  def props(
            waiter: ActorRef,
            favouriteCoffee: Coffee,
            finishCoffeeDuration: FiniteDuration,
            caffeineLimit: Int): Props =
    Props(new Guest(waiter, favouriteCoffee, finishCoffeeDuration, caffeineLimit))
}

// Waiter would be of type waiter but because Actors are protected we must pass the ActorRef
class Guest(
            waiter: ActorRef,
            favouriteCoffee: Coffee,
            finishCoffeeDuration: FiniteDuration,
            caffeineLimit: Int)
  extends Actor
    with ActorLogging
    with Timers {
  import Guest._

  private var coffeeCount: Int = 0 // best practice to make vars in Actors private
  // even though the Actor cannot be accessed !!!

  // need the guest to order a coffee when arrives
  orderCoffee()

  override def receive: Receive = {
    case Waiter.CoffeeServed(coffee) =>
      coffeeCount += 1
      // sends CoffeeFinished message to ourselves after finishCoffeeDuration
      timers.startSingleTimer(s"coffee-finished", CoffeeFinished, finishCoffeeDuration)
      log.info(s"Enjoying my $coffeeCount yummy $coffee")
    case CoffeeFinished if coffeeCount > caffeineLimit =>
      throw CaffeineException
    case CoffeeFinished =>
      log.info(s"Just finished my $coffeeCount yummy coffee")
      orderCoffee()
  }

  private def orderCoffee(): Unit = {
    waiter ! Waiter.ServeCoffee(favouriteCoffee)
  }

  override def postStop(): Unit = {
    log.info("Goodbye!")
    super.postStop()
  }
}
