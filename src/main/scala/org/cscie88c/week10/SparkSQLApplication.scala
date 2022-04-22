package org.cscie88c.week10

import org.apache.spark.sql.SparkSession
import com.typesafe.scalalogging.{ LazyLogging }
import org.cscie88c.config.{ ConfigUtils }
import org.cscie88c.utils.{ SparkUtils }
import org.apache.spark.sql.{ DataFrame, Dataset, Row }
import pureconfig.generic.auto._
import org.apache.spark.sql.functions.{ col, lit, when }

// run with: sbt "runMain org.cscie88c.week10.SparkSQLApplication"
object SparkSQLApplication {

  def main(args: Array[String]): Unit = {
    implicit val conf: SparkDSConfig = readConfig()
    val spark = SparkUtils.sparkSession(conf.name, conf.masterUrl)
    val transactionDF = loadData(spark)
    val augmentedTransactionsDF = addCategoryColumn(transactionDF)
    augmentedTransactionsDF.createOrReplaceTempView("transactions")
    val sparkSQL =
      "select category,sum(tran_amount) as total from transactions group by category"
    val totalsByCategoryDF = spark.sql(sparkSQL)
    printTransactionTotalsByCategory(totalsByCategoryDF)
    spark.stop()
  }

  def readConfig(): SparkDSConfig = ConfigUtils.loadAppConfig[SparkDSConfig](
    "org.cscie88c.spark-ds-application"
  )

  def loadData(spark: SparkSession)(implicit conf: SparkDSConfig): DataFrame = {
    val transactionDFFromFile =
      spark
        .read
        .format("csv")
        .option("header", "true")
        .option("inferSchema", "true")
        .load(conf.transactionFile)
    transactionDFFromFile
  }

  def addCategoryColumn(raw: DataFrame): DataFrame = raw.withColumn(
    "category",
    when(col("tran_amount") > 80, lit("High")).otherwise(lit("Standard"))
  )

  def printTransactionTotalsByCategory(df: DataFrame): Unit = {
    println(s"Transaction totals by category:")
    df.collect.foreach { x =>
      val category = x.getString(0)
      val total = x.getLong(1)
      println(s"Category: $category, Total: $total")
    }
  }

}
