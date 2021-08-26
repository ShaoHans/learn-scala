package com.shz.wordcount

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("WordCount")
        conf.setMaster("local")

        val context = new SparkContext(conf)
        val fileRDD = context.textFile("data/test.txt")
        val wordCountRDD = fileRDD.flatMap(line => line.split(",")).map(word => Tuple2(word, 1)).reduceByKey((oldV, newV) => oldV + newV)
        val wordCountCountRDD = wordCountRDD.map(wordCount => Tuple2(wordCount._2, 1)).reduceByKey((oldV, newV) => oldV + newV)
        wordCountCountRDD.foreach(println)
        wordCountRDD.foreach(println)

        // 简化版
        //fileRDD.flatMap(_.split(",")).map((_, 1)).reduceByKey(_ + _).foreach(println)

        Thread.sleep(Long.MaxValue)
    }
}
