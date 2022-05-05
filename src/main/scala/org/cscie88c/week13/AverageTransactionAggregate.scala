package org.cscie88c.week13


import cats._
import java.text.SimpleDateFormat

//define data structure for aggregated MLS data
final case class AverageTransactionAggregate(
    timeKey: String,
    totalAmount: Long,
    count: Long
  ) {
  def averageAmount: Long = totalAmount / count
}

object AverageTransactionAggregate {
  val formatTimeKey = new SimpleDateFormat("yyyy-mm")
  val formatDate = new SimpleDateFormat("mm/dd/yyyy")
  def apply(raw: MLSTransaction): AverageTransactionAggregate =
    AverageTransactionAggregate(formatTimeKey.format(formatDate.parse(raw.soldDate)) +"-"+ raw.proptype , raw.soldPrice.getOrElse(0L), 1L)

  //implement monoid for MLS transaction  
  implicit val averageTransactionMonoid: Monoid[AverageTransactionAggregate] =
    new Monoid[AverageTransactionAggregate] {
      override def empty: AverageTransactionAggregate =
        AverageTransactionAggregate("", 0L, 0L)

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
