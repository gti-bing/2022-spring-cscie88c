package org.cscie88c.week11

import cats._
import cats.implicits._
import com.fasterxml.jackson.module.scala.deser.overrides

final case class WritableRow(
    customerId: String,
    averageAmount: Double
  )

final case class AverageTransactionAggregate(
    customerId: String,
    totalAmount: Double,
    count: Long
  ) {
  def averageAmount: Double = totalAmount / count
}

object AverageTransactionAggregate {
  def apply(raw: RawTransaction): AverageTransactionAggregate =
    AverageTransactionAggregate(raw.customer_id, raw.tran_amount, 1)

  implicit val averageTransactionMonoid: Monoid[AverageTransactionAggregate] =  
    new Monoid[AverageTransactionAggregate] {
      override def empty: AverageTransactionAggregate =
        AverageTransactionAggregate("", 0, 0)
      override def combine(
          x: AverageTransactionAggregate,
          y: AverageTransactionAggregate
        ): AverageTransactionAggregate =
        AverageTransactionAggregate(
          if (x.customerId.isEmpty()) y.customerId else x.customerId,
          x.totalAmount + y.totalAmount,
          x.count + y.count
        )
    } 
}

