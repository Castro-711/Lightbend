/**
 * Copyright © 2014 - 2020 Lightbend, Inc. All rights reserved. [http://www.lightbend.com]
 */

package com.lightbend.training

import scala.concurrent.duration.FiniteDuration

/**
  * Package objects are a convenient container shared across an entire package
  *
  * They can contain arbitrary definitions, not just just variable & method defs.
  * They are often used to hold package-wide type aliases & implicit conversions.
  * Package objects can even inherit Scala classes & traits.
  *
  * By convention, it lives in a source file called package.scala.
  * Each package is allowed to have one package object.
  * Andy defs placed in the package object are considered members of the package itself.
  *
  * They are just like other objects - meaning can used inheritance for building them.
  */

package object coffeehouse {

  type Iterable[+A] = scala.collection.immutable.Iterable[A]

  type Seq[+A] = scala.collection.immutable.Seq[A]

  type IndexedSeq[+A] = scala.collection.immutable.IndexedSeq[A]

  /**
   * Keeps the CPU busy for the given approximate duration.
   */
  def busy(duration: FiniteDuration): Unit =
    pi(System.nanoTime() + duration.toNanos)

  /**
   * Calculate pi until System.nanoTime is later than `endNanos`
   */
  private def pi(endNanos: Long) = {
    def gregoryLeibnitz(n: Long) = 4.0 * (1 - (n % 2) * 2) / (n * 2 + 1)
    var n = 0
    var acc = BigDecimal(0.0)
    while (System.nanoTime() < endNanos) {
      acc += gregoryLeibnitz(n)
      n += 1
    }
    acc
  }
}
