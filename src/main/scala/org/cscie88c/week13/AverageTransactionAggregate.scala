package org.cscie88c.week13


import cats._
import cats.implicits._
import java.text.SimpleDateFormat

final case class AverageTransactionAggregate(
    timeKey: String,
    totalAmount: Double,
    count: Long
  ) {
  def averageAmount: Double = totalAmount / count
}

object AverageTransactionAggregate {
  val formatTimeKey = new SimpleDateFormat("yyyy-mm")
  val formatDate = new SimpleDateFormat("mm/dd/yyyy")
  def apply(raw: MLSTransaction): AverageTransactionAggregate =
    AverageTransactionAggregate("01/01/1900", 0.0, 1L)

  implicit val averageTransactionMonoid: Monoid[AverageTransactionAggregate] =
    new Monoid[AverageTransactionAggregate] {
      override def empty: AverageTransactionAggregate =
        AverageTransactionAggregate("", 0.0, 0L)

      override def combine(
          x: AverageTransactionAggregate,
          y: AverageTransactionAggregate
        ): AverageTransactionAggregate =
        AverageTransactionAggregate(
          x.timeKey,
          x.totalAmount + y.totalAmount,
          x.count + y.count
        )
    }
}
