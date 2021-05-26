package com.hoko.wordcount

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("WordCount")
    conf.setMaster("local")

    val context = new SparkContext(conf)
    val fileRDD = context.textFile("data/test.txt")
    fileRDD.flatMap(_.split(",")).map((_, 1)).reduceByKey(_ + _).foreach(println)
  }
}
