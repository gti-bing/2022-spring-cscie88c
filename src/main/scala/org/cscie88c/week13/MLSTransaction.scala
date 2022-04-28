package org.cscie88c.week13

import java.util.Date
import java.text.SimpleDateFormat
import scala.util.{ Try }

final case class MLSTransaction(
    id: Int,
    mlsNum: Long,
    status: String,
    listPrice: Long,
    soldPrice: Option[Long],
    listDate: String,
    soldDate: String,
    address: String,
    city: String,
    state: String,
    zip: String,
    beds: Int,
    baths: Int,
    sqft: Int,
    age: Int,
    lotsize: Int,
    proptype: String

)

object MLSTransaction {
    def apply(csvRow: String): Option[MLSTransaction] = Try {
    val fields = csvRow.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")
    MLSTransaction(
      id = fields(0).toInt,
      mlsNum = fields(1).toLong,
      status = fields(2),
      listPrice = fields(3).toLong,
      soldPrice = fields(4).toLongOption,
      listDate = fields(5),
      soldDate = fields(6),
      address = fields(10),
      city = fields(11),
      state = fields(12),
      zip = fields(13),
      beds = fields(15).toInt,
      baths = fields(16).toInt,
      sqft = fields(17).toInt,
      age = fields(18).toInt,
      lotsize = fields(19).toInt,
      proptype = fields(34)
    )
  }.toOption
}