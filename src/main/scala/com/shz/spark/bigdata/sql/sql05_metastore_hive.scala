package com.shz.spark.bigdata.sql

import org.apache.spark.sql.SparkSession

object sql05_metastore_hive {
    def main(args: Array[String]): Unit = {
        val session = SparkSession.builder()
            .appName("sql05_metastore_hive")
            .master("local")
            .config("spark.sql.shuffle.partitions", "1")
            .config("hive.metastore.uris", "thrift://node01:9083")
            .enableHiveSupport()
            .getOrCreate()
        session.sparkContext.setLogLevel("ERROR")

    }
}
