package com.shz.bigdata.rddapi

import org.apache.spark.{SparkConf, SparkContext};

object Rdd_05_Sample_RePartition {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Rdd_05_Sample_RePartition").setMaster("local")
        val sc = new SparkContext(conf)
        sc.setLogLevel("ERROR")

        val data = sc.parallelize(1 to 20, numSlices = 5)
        // 抽样
        // false：样本数据不能重复，true：样本数据可以重复；seed值一样，则产生的样本数据一样
        /*data.sample(withReplacement = false, 0.5, seed = 123).foreach(println)
        println("-----------")
        data.sample(withReplacement = false, 0.5, seed = 123).foreach(println)
        println("-----------")
        data.sample(withReplacement = true, 0.5).foreach(println)*/

        val res = data.mapPartitionsWithIndex((i, iter) => iter.map(e => (i, e)))
        res.foreach(println)
        println("-----------")
        res.repartition(6).mapPartitionsWithIndex((i,iter)=>iter.map(e=>(i,e))).foreach(println)
        println("-----------")
        res.coalesce(numPartitions = 3,shuffle = false).mapPartitionsWithIndex((i,iter)=>iter.map(e=>(i,e))).foreach(println)

        while (true) {

        }
    }
}
