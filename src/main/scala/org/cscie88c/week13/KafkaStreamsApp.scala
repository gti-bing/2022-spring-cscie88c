package org.cscie88c.week13

import java.time.Duration
import java.util.Properties
import com.typesafe.scalalogging.{LazyLogging}
import org.cscie88c.config.{ConfigUtils}
import pureconfig.generic.auto._
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{ KafkaStreams, StreamsConfig }
import java.util.logging.SimpleFormatter
import java.text.SimpleDateFormat
import com.goyeau.kafka.streams.circe.CirceSerdes._
import io.circe.generic.auto._

case class KafkaStreamsAppConfig(
  name: String,
  inputTopicName: String,
  outputTopicName: String,
  bootstrapServerUrl: String
)



// run with: sbt "runMain org.cscie88c.week13.KafkaStreamsApp"
object KafkaStreamsApp extends LazyLogging{

  def SomeTransaction(tuple: (String,Option[MLSTransaction])): (String, MLSTransaction) = {
    tuple match {
      case (first, Some(second)) => (first, second)
    }
  }

  def main(args: Array[String]): Unit = {
    import ImplicitConversions._
    import Serdes._
    
    val appSettings = ConfigUtils.loadAppConfig[KafkaStreamsAppConfig]("org.cscie88c.simple-kafkastreams-app")
    
    // 1. define kafka streams properties, usually from a config file
    val props: Properties = {
      val p = new Properties()
      p.put(StreamsConfig.APPLICATION_ID_CONFIG, appSettings.name)
      p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, appSettings.bootstrapServerUrl)
      p
    }

    // 2. create KStreams DSL instance
    val builder: StreamsBuilder = new StreamsBuilder
    val textLines: KStream[String, String] =
      builder.stream[String, String](appSettings.inputTopicName)

    // 3. transform the data 
    val format = new SimpleDateFormat("yyyy-mm")
     val monthlyAverageKStream: KStream[String,MLSTransaction] = textLines
       .map((k,v) => (k, MLSTransaction(v)))
       .filter((_,v) => v.isDefined)
       .map((k,v) => SomeTransaction(k,v))
       .filter((_,v) => v.soldDate.length>0)
      // .map((_,v) => (v.soldDate,v.soldPrice.getOrElse(0L).toString))
      //.map((_,v) => (v.soldDate,v.proptype))
     
    //   monthlyAverageKStream.to(appSettings.outputTopicName)
      monthlyAverageKStream.to(appSettings.outputTopicName)
    // val monthlyAverageKTable: KTable[String,Double] = monthlyAverageKStream
    //   .groupBy((k,v) => k)
    //   .reduce((a,b) => a + b )(Materialized.as("counts-store"))


    // val wordCounts: KTable[String, Long] = textLines
    //   .flatMapValues(textLine => textLine.toLowerCase.split("\\W+"))
    //   .groupBy((_, word) => word)
    //   .count()(Materialized.as("counts-store"))

    // 4. write the results to a topic or other persistent store
    // wordCounts
    //   .toStream
    //   // .peek((k,t) => println(s"stream element: $k: $t")) // to print items in stream
    //   .filter((_, count) => count > 5)
    //   .map((word, count) => (word, s"$word: $count"))
    //   .to("WordsWithCountsTopic")

    // 5. start the streams application
    val streams: KafkaStreams = new KafkaStreams(builder.build(), props)
    streams.start()

    // 6. attach shutdown handler to catch control-c
    sys.ShutdownHookThread {
      streams.close(Duration.ofSeconds(10))
    }
  }

}
