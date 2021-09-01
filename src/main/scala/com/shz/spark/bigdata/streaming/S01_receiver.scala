package com.shz.spark.bigdata.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object S01_receiver {
    def main(args: Array[String]): Unit = {
        // [2]可并行跑2个任务
        // 1个给receiverjob的task，
        // 另一个给batch计算的job（只不过如果batch比较大，你期望n>2,因为多出来的线程可以跑并行的batch@job@task）
        val conf = new SparkConf().setAppName("S01_receiver").setMaster("local[2]")

        //微批的流式计算，每隔指定时间触发一次job
        // 如果某个批次执行的时间比较久，会阻塞后面批次的执行
        val context = new StreamingContext(conf, Seconds(5))
        context.sparkContext.setLogLevel("ERROR")

        // 在Linux系统上使用nc客户端进行连接测试： nc -l 8888
        val receiver = context.socketTextStream("172.28.93.145", 8888)
        receiver.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).print()

        context.start()
        context.awaitTermination()
    }
}
