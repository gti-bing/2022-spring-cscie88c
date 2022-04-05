package org.cscie88c.week11

import cats._
import cats.implicits._

final case class WritableRow(
  customerId: String,
  averageAmount: Double
)

final case class AverageTansactionAggregate(
  customerId: String,
  totalAmount: Double,
  count: Long
) {
  def averageAmount: Double = totalAmount / count
}


object AverageTansactionAggregate {
  def apply(raw: RawTransaction): AverageTansactionAggregate = ???

  implicit val averageTransactionMonoid: Monoid[AverageTansactionAggregate] = ???
}