package com.shz.spark.bigdata.sql

import org.apache.spark.sql.SparkSession

object sql06_function {
    def main(args: Array[String]): Unit = {
        val session = SparkSession.builder()
            .appName("sql06_function")
            .master("local")
            .config("spark.sql.shuffle.partitions", "1")
            .config("spark.sql.warehouse.dir", "d:/spark/warehouse")
            .enableHiveSupport()
            .getOrCreate()
        session.sparkContext.setLogLevel("ERROR")

        import session.implicits._

       var data =  List(
            ("A",1,56),
            ("A",2,96),
            ("B",1,78),
            ("B",2,82),
            ("C",1,75),
        ).toDF("name","type","score")
        data.createTempView("stu")
        data.show()

        // 1.自定义函数
        session.udf.register("byten",(x:Int)=>{x*10})
        session.sql("select * ,byten(score) from stu").show()

    }
}
