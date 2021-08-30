package com.shz.spark.bigdata.rddapi

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer;

object Rdd_04_Partitions {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Rdd_04_Partitions").setMaster("local")
        val sc = new SparkContext(conf)
        sc.setLogLevel("ERROR")

        // 2个分区
        val data = sc.parallelize(1 to 10, 2)

        // 时刻要知道，map里面的函数逻辑是要分发到各个分区执行
        // 1.每迭代一次就要打开关闭数据库连接，严重影响性能
        data.map(v => {
            openConn()
            val str = query(v)
            closeConn()
            str
        }).foreach(println)

        // 2.只打开关闭一次数据库连接
        data.mapPartitionsWithIndex((index, iter) => {
            openConn(index)
            val strings = new ListBuffer[String]() // 占用大量内存，会产生OOM，绝对不可取
            while (iter.hasNext) {
                strings.append(query(iter.next()))
            }
            closeConn(index)
            strings.iterator
        }).foreach(println)

        // 3.通过迭代器模式解决OOM
        data.mapPartitionsWithIndex((index, iter) => {
            openConn(index)
            new Iterator[String] {
                override def hasNext: Boolean = if (iter.hasNext) true else {
                    closeConn(index)
                    false
                }

                override def next(): String = {
                    query(iter.next())
                }
            }
        }).foreach(println)

        while (true) {

        }
    }

    def openConn(pIndex: Int = 0): Unit = {
        println(s"$pIndex open conn...")
    }

    def closeConn(pIndex: Int = 0): Unit = {
        println(s"$pIndex close conn...")
    }

    def query(id: Int): String = {
        id + "-query"
    }
}
