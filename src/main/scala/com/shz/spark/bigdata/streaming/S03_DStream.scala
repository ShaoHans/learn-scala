package com.shz.spark.bigdata.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object S03_DStream {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("S03_DStream").setMaster("local[2]")
        val ssc = new StreamingContext(conf, Seconds(1))
        ssc.sparkContext.setLogLevel("ERROR")

        val receiver = ssc.socketTextStream("localhost", 8888)
        receiver.flatMap(_.split(" ")).map(e=>(e))

        ssc.start()
        ssc.awaitTermination()
    }
}
