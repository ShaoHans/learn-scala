package com.shz.bigdata.rddapi

import org.apache.spark.{SparkConf, SparkContext};

object Rdd_01 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("api01").setMaster("local")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    val rdd1 = sc.parallelize(List(1, 2, 3, 4, 5))
    val rdd2 = sc.parallelize(List(3, 4, 5, 6, 7))
//    println(rdd1.partitioner.size)
//    println(rdd2.partitioner.size)
    val unionRdd = rdd1.union(rdd2)
//    println(unionRdd.partitioner.size)
    println("----------全集----------")
    unionRdd.foreach(println)

    println("----------交集----------")
    val interst = rdd1.intersection(rdd2)
    interst.foreach(println)

    println("----------差集----------")
    val leftSub = rdd1.subtract(rdd2)
    leftSub.foreach(println)

    println("----------笛卡尔积----------")
    val cartesian = rdd1.cartesian(rdd2)
    cartesian.foreach(println)

    val k1 = sc.parallelize(List(("tom", 1), ("tom", 2), ("lucy", 3), ("hans", 4)))
    val k2 = sc.parallelize(List(("tom", 5), ("tom", 6), ("lucy", 7), ("jim", 8)))

    println("----------cogroup----------")
    val cog = k1.cogroup(k2)
    cog.foreach(println)

    println("----------join----------")
    val join = k1.join(k2)
    join.foreach(println)

    println("----------leftjoin----------")
    val leftjoin = k1.leftOuterJoin(k2)
    leftjoin.foreach(println)

    println("----------rightjoin----------")
    val rightjoin = k1.rightOuterJoin(k2)
    rightjoin.foreach(println)

    println("----------fulljoin----------")
    val fulljoin = k1.fullOuterJoin(k2)
    fulljoin.foreach(println)

    while (true){

    }
  }
}
