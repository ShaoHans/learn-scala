package com.shz.spark.bigdata.rddapi

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable;

object Rdd_06_TopN {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Rdd_06_TopN").setMaster("local")
        val sc = new SparkContext(conf)
        sc.setLogLevel("ERROR")
        val fileRDD = sc.textFile("data/topn")
        val dataRDD = fileRDD.map(line => {
            val strings = line.split(",")
            val date = strings(0).split("-")
            (date(0).toInt, date(1).toInt, date(2).toInt, strings(1).toInt)
        })
        //dataRDD.foreach(println)
        // 分组取TopN（取每年月温度最高的2天）
        // 第一种方案：按照"年"和"月"为key进行分组，values为"日"和"温度"，对每个values进行去重然后排序
        // 弊端：groupByKey，一旦数据量超大，容易造成OOM，慎用！！！
        dataRDD.map(e => ((e._1, e._2), (e._3, e._4)))
            .groupByKey()
            .mapValues(e => {
                // 按"日"去重，取那"日"最高"温度"。数据量大的时候，申请的HashMap内存资源容易造成OOM
                val hm = new mutable.HashMap[Int, Int]()
                e.foreach(v => {
                    if (hm.getOrElse(v._1, 0) < v._2)
                        hm.put(v._1, v._2)
                })
                // 取每月前2日的最高温
                hm.toList.sortWith((v1, v2) => v2._2 < v1._2).take(2)
            }).foreach(println)

        println("-------------------")
        // 第二种方案：按照年月日去重留下温度最高的，再按照年月分组，保留温度最高的2天
        // 弊端：仍然使用了groupByKey
        dataRDD.map(e => ((e._1, e._2, e._3), e._4))
            .reduceByKey((oldWd, newWd) => if (newWd > oldWd) newWd else oldWd)
            .map(e => ((e._1._1, e._1._2), (e._1._3, e._2)))
            .groupByKey()
            .mapValues(v => v.toList.sortWith((v1, v2) => v2._2 < v1._2).take(2))
            .foreach(println)

        println("-------------------")
        // 第三种方案：先按照 年月温度 进行全排序，再去重取每日最大温度。
        // 破坏了子集的排序， 数据会错乱
        /*dataRDD.sortBy(e => (e._1, e._2, e._4), ascending = false)
            .map(e => ((e._1, e._2, e._3), e._4))
            .reduceByKey((oldWd, newWd) => if (newWd > oldWd) newWd else oldWd)
            .map(e => ((e._1._1, e._1._2), (e._1._3, e._2)))
            .groupByKey()
            .foreach(println)*/

        println("-------------------")
        // 第四种方案：先按照 年月温度 进行全排序，再去重取每日最大温度。
        // map阶段排好序，reduce进行二次排序，没有破坏子集的排序
        /*dataRDD.sortBy(e => (e._1, e._2, e._4), ascending = false)
            .map(e => ((e._1, e._2), (e._3, e._4)))
            .groupByKey()
            .foreach(println)*/

        println("-------------------")
        // 最终方案，combineByKey调优天下无敌
        implicit val o = new Ordering[(Int, Int)] {
            override def compare(x: (Int, Int), y: (Int, Int)): Int = y._2.compare(x._2)
        }

        /*dataRDD.map(e => ((e._1, e._2), (e._3, e._4)))
            .combineByKey(
                v => mutable.SortedSet((v._1, v._2), (0, 0)),
                (oldV: mutable.SortedSet[(Int, Int)], newV) => {
                    var head = oldV.head
                    var tail = oldV.last

                    if(head._2 < newV._2){
                        oldV.dropRight(1)

                    }
                }

            )*/

        while (true) {

        }
    }
}
