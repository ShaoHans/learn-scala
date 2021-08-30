package com.shz.spark.bigdata.sql

import org.apache.spark.sql.SparkSession

object sql04_standalone_hive {
    def main(args: Array[String]): Unit = {
        val session = SparkSession.builder()
            .appName("sql04_standalone_hive")
            .master("local")
            .config("spark.sql.shuffle.partitions", "1")
            .config("spark.sql.warehouse.dir", "d:/spark/warehouse")
            .enableHiveSupport()
            .getOrCreate()
        session.sparkContext.setLogLevel("ERROR")

        session.sql("create database school")
        session.sql("use school")
        session.sql("create table student(id int ,name string)")
        session.catalog.listTables().show()
    }
}
