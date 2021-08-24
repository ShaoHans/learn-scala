package com.shz.bigdata.rddapi

import org.apache.spark.{SparkConf, SparkContext};

object Rdd_04_Partitions {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Rdd_04_Partitions").setMaster("local")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    // 2个分区
    val data = sc.parallelize(1 to 20, 2)

    while (true) {

    }
  }
}
