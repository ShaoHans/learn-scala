package com.shz.spark.bigdata.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

import java.util.Properties

object sql03_jdbc {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("sql03_jdbc").setMaster("local")
        val session = SparkSession.builder()
            .config(conf)
            .config("spark.sql.shuffle.partitions", "1")
            .getOrCreate()
        val sc = session.sparkContext
        sc.setLogLevel("ERROR")

        //saveToParquet(session)

        val properties = new Properties()
        properties.put("url", "jdbc:mysql://root@localhost:3306/test?serverTimezone=UTC")
        properties.put("user", "root")
        properties.put("password", "123456")
        properties.put("driver", "com.mysql.cj.jdbc.Driver")

        val df = session.read.jdbc(properties.get("url").toString, "test", properties)
        df.createTempView("person")
        session.sql("select * from person").show()
        df.write.jdbc(properties.get("url").toString, "testbak", properties)
    }

    def saveToParquet(session: SparkSession): Unit = {
        val df = session.read.textFile("data/test.txt").toDF("line")
        df.createTempView("wc");
        //session.sql(" select t.word,count(*) count from (select explode(split(line,',')) word from wc) t group by t.word ").show()
        val frame = df.selectExpr("explode(split(line,',')) word").groupBy("word").count()
        frame.write.mode(SaveMode.Overwrite).parquet("output/wordcount")
        session.read.parquet("output/wordcount").show()
    }
}
