package com.shz.bigdata.rddapi

import org.apache.spark.{SparkConf, SparkContext};

object Rdd_02_PVUV_Sort {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("api02").setMaster("local")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    val fileRDD = sc.textFile("data/pvuvdata", 5)
    // 统计pv排名前五的网站及访问量
    println("------统计pv排名前五的网站及访问量-------")
    fileRDD.map(line => (line.split("\t")(5), 1)).reduceByKey(_ + _).map(m => m.swap).sortByKey(ascending = false).take(5).map(m => m.swap).foreach(println)

    // 统计uv排名前五的网站及访问量
    println("------统计uv排名前五的网站及访问量-------")
    fileRDD.map(line => {
      val arr = line.split("\t")
      (arr(5), arr(0))
    }).distinct().map(m => (m._1, 1)).reduceByKey(_ + _).sortBy(m => m._2, ascending = false).take(5).foreach(println)

    while (true) {

    }
  }
}
