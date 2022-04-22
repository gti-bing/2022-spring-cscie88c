package org.cscie88c.week7

import scala.io.Source
import scala.util.{ Failure, Success, Try }

final case class CustomerTransaction(
    customerId: String,
    transactionDate: String,
    transactionAmount: Double
  )

object CustomerTransaction {
  // add companion object methods below
}
