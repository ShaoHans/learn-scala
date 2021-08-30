package com.shz.spark.bigdata.rddapi

import org.apache.spark.{SparkConf, SparkContext};

/**
 * map：           一进一出
 * mapValues：     可单独对每个元素的values进行处理，按照列格式输出 =》(k1,List(v11,v12)),(k2,List(v21,v22))，优化：key不会发生变化，不产生shuffle
 * flatMap：       一进多出，对元素的key和value一起处理
 * flatMapValues： 可单独对每个元素的values进行处理，按照行格式输出 =》(k1,v11),(k1,v12),(k2,v21),(k2,v22)，优化：key不会发生变化，不产生shuffle
 */
object Rdd_03_Aggregation {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("api01").setMaster("local")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")

    val data = sc.parallelize(List(("tom", 99), ("tom", 80), ("tom", 97), ("lucy", 50), ("lucy", 82), ("lucy", 10), ("jim", 100), ("jim", 36)))
    println("-----行转列-----")
    val group = data.groupByKey()
    group.foreach(println)
    println("-----列转行1-----")
    group.flatMap(e => e._2.map(s => (e._1, s)).iterator).foreach(println)
    println("-----列转行2-----")
    group.flatMapValues(e => e.iterator).foreach(println)

    println("-----每个人取前2名的成绩-----")
    group.mapValues(e => e.toList.sortWith((i1, i2) => i1 > i2).take(2)).foreach(println)

    println("-----sum-----")
    val sum = data.reduceByKey(_ + _)
    sum.foreach(println)

    println("-----max-----")
    data.reduceByKey((oldVal, newVal) => if (oldVal < newVal) newVal else oldVal).foreach(println)

    println("-----min-----")
    data.reduceByKey((oldVal, newVal) => if (oldVal < newVal) oldVal else newVal).foreach(println)

    println("-----count-----")
    val count = data.map(m => (m._1, 1)).reduceByKey(_ + _)
    count.foreach(println)

    println("-----avg-----")
    sum.join(count).mapValues(m => m._1 / m._2).foreach(println)

    println("-----avg combine-----")
    data.combineByKey(
      //      createCombiner: V => C,
      (score: Int) => (score, 1),
      //      mergeValue: (C, V) => C,
      (oldValue: (Int, Int), newScore: Int) => (oldValue._1 + newScore, oldValue._2 + 1),
      //      mergeCombiners: (C, C) => C,
      (oldValue: (Int, Int), newValue: (Int, Int)) => (oldValue._1 + newValue._1, oldValue._2 + newValue._2)
    ).mapValues(e => e._1 / e._2).foreach(println)

    while (true) {

    }
  }
}
