package com.shz.spark.bigdata.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

import java.io.Serializable
import scala.beans.BeanProperty

object Sql02_Dataset {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Sql02_Dataset").setMaster("local")
        val session = SparkSession.builder()
            .config(conf)
            //            .appName("Sql01_Basic")
            //            .master("local")
            //            .enableHiveSupport()  // 开启这个选项时  spark sql on  hive  才支持DDL，没开启，spark只有catalog
            .getOrCreate()
        val sc = session.sparkContext
        sc.setLogLevel("ERROR")

        //createDataFrame01(session)
        //createDataFrame02(session)
        //createDataFrame03(session)
        createDataFrame04(session)
    }

    /**
     * 根据文本数据手动创建表的Schema
     *
     * @param session
     */
    def createDataFrame01(session: SparkSession): Unit = {
        val fileRDD = session.sparkContext.textFile("data/person.txt")
        // 1.得到RDD[Row]
        val rowRDD = fileRDD.map(line => {
            val strings = line.split(",")
            Row.apply(strings(0), strings(1).toInt)
        })
        // 2.得到structType
        val fields = Array(
            StructField.apply("name", DataTypes.StringType),
            StructField.apply("age", DataTypes.IntegerType)
        )
        val structType = StructType.apply(fields)
        val df = session.createDataFrame(rowRDD, structType)
        df.show()
        df.printSchema()
        df.createTempView("person")
        session.sql("select * from person").show()
    }

    /**
     * 动态创建表的Schema
     *
     * @param session
     */
    def createDataFrame02(session: SparkSession): Unit = {
        val fieldSchema = Array(
            "name string",
            "age int"
        )

        def toDataType(tuple: (String, Int)) = {
            fieldSchema(tuple._2).split(" ")(1) match {
                case "string" => tuple._1.toString
                case "int" => tuple._1.toInt
            }
        }

        def getDataType(str: String): DataType = {
            str match {
                case "string" => DataTypes.StringType
                case "int" => DataTypes.IntegerType
            }
        }

        val fileRDD = session.sparkContext.textFile("data/person.txt")
        val rowRDD = fileRDD.map(_.split(",").zipWithIndex).map(e => e.map(toDataType(_))).map(e => Row.fromSeq(e))
        val structType = StructType.apply(fieldSchema.map(_.split(" ")).map(e => StructField.apply(e(0), getDataType(e(1)))))
        //val structType = StructType.fromDDL("name string,age int")
        val df = session.createDataFrame(rowRDD, structType)
        df.show()
        df.printSchema()
        df.createTempView("person")
        session.sql("select * from person").show()
    }

    /**
     * 根据classBean创建表的Schema
     *
     * @param session
     */
    def createDataFrame03(session: SparkSession): Unit = {
        class Person extends Serializable {
            @BeanProperty
            var name: String = ""
            @BeanProperty
            var age: Int = 0
        }
        val fileRDD = session.sparkContext.textFile("data/person.txt")
        val personRDD = fileRDD.map(_.split(",")).map(e => {
            val person = new Person
            person.setName(e(0))
            person.setAge(e(1).toInt)
            person
        })
        val df = session.createDataFrame(personRDD, classOf[Person])
        df.show()
        df.printSchema()
        df.createTempView("person")
        session.sql("select * from person").show()
    }

    /**
     * Dataset
     *
     * @param session
     */
    def createDataFrame04(session: SparkSession): Unit = {
        import session.implicits._
        val ds = session.read.textFile("data/person.txt")
        val personDs = ds.map(e => {
            val strings = e.split(",")
            (strings(0), strings(1).toInt)
        })

        val df = personDs.toDF("name", "age")
        df.show()
        df.printSchema()
        df.createTempView("person")
        session.sql("select * from person").show()
    }
}
