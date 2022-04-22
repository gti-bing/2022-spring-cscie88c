package org.cscie88c.utils

import org.apache.spark.sql.SparkSession

object SparkUtils {

  def sparkSession(
      appName: String = "spark-app",
      masterURL: String = "local[*]"
    ): SparkSession = {
    lazy val spark = SparkSession
      .builder()
      .appName(appName)
      .master(masterURL)
      .config("dfs.client.read.shortcircuit.skip.checksum", "true")
      .config("mapreduce.fileoutputcommitter.marksuccessfuljobs", "false")
      .getOrCreate()

    spark
  }

}
