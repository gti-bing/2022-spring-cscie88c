package org.cscie88c.week10

import org.apache.spark.sql.SparkSession
import com.typesafe.scalalogging.{ LazyLogging }
import org.cscie88c.config.{ ConfigUtils }
import org.cscie88c.utils.{ SparkUtils }
import org.apache.spark.sql.{ Dataset }
import pureconfig.generic.auto._
import org.apache.spark.sql.types._

// write config case class below
case class SparkDSConfig(
    name: String,
    masterUrl: String,
    transactionFile: String
  )

// run with: sbt "runMain org.cscie88c.week10.SparkDSApplication"
object SparkDSApplication {

  // application main entry point
  def main(args: Array[String]): Unit = {
    implicit val conf: SparkDSConfig = readConfig()
    val spark = SparkUtils.sparkSession(conf.name, conf.masterUrl)
    val transactionDS = loadData(spark)
    val totalsByCategoryDS = transactionTotalsByCategory(spark, transactionDS)
    printTransactionTotalsByCategory(totalsByCategoryDS)
    spark.stop()
  }

  def readConfig(): SparkDSConfig = ConfigUtils.loadAppConfig[SparkDSConfig](
    "org.cscie88c.spark-ds-application"
  )

  def loadData(
      spark: SparkSession
    )(implicit
      conf: SparkDSConfig
    ): Dataset[CustomerTransaction] = {
    import spark.implicits._
    val customerTransactionDSFromFile =
      spark
        .read
        .format("csv")
        .option("header", "true")
        .option("inferSchema", "true")
        .load(conf.transactionFile)
        .as[RawTransaction]
        .map(CustomerTransaction(_))
    customerTransactionDSFromFile
  }

  def transactionTotalsByCategory(
      spark: SparkSession,
      transactions: Dataset[CustomerTransaction]
    ): Dataset[(String, Double)] = {
    import spark.implicits._
    transactions
      .map(x => (x.transactionCategory, x.transactionAmount))
      .groupByKey(_._1)
      .reduceGroups((a, b) => (a._1, a._2 + b._2))
      .map(_._2)
  }

  def printTransactionTotalsByCategory(ds: Dataset[(String, Double)]): Unit = {
    println(s"Transaction totals by category:")
    ds.foreach(x => println(s"Category: ${x._1}, Total: ${x._2}"))
  }
}
