package com.shz.spark.bigdata.sql

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}


object Sql01_Basic {

    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Sql01_Basic").setMaster("local")
        val session = SparkSession.builder()
            .config(conf)
            //            .appName("Sql01_Basic")
            //            .master("local")
            //            .enableHiveSupport()  // 开启这个选项时  spark sql on  hive  才支持DDL，没开启，spark只有catalog
            .getOrCreate()
        val sc = session.sparkContext
        sc.setLogLevel("ERROR")

        /*session.catalog.listDatabases().show()
        session.catalog.listTables().show()
        session.catalog.listFunctions().show(10)
        println("-------------------------------")*/

        val frame = session.read.json("data/person.json")
        frame.printSchema()
        frame.createTempView("person")
        session.sql("select * from person").show()

        import scala.io.StdIn._
        while (true){
            val sql = readLine("input your sql :")
            session.sql(sql).show()
        }
    }
}
