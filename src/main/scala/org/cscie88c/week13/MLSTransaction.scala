package org.cscie88c.week13

import scala.util.{ Try }

//define data struct for MLS data
final case class MLSTransaction (
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
    proptype: String
)

object MLSTransaction {
    // load csv raw data into corresponding fields in the MLSTransction
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
      proptype = fields(34)
    )
  }.toOption
}