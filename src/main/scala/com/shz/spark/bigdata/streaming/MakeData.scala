package com.shz.spark.bigdata.streaming

import java.io.PrintStream
import java.net.ServerSocket

object MakeData {
    def main(args: Array[String]): Unit = {
        val server = new ServerSocket(8888)
        println("server started")
        while (true) {
            val client = server.accept()

            new Thread() {
                override def run(): Unit = {
                    var num = 0
                    if (client.isConnected) {
                        val output = new PrintStream(client.getOutputStream)
                        while (client.isConnected) {
                            num += 1
                            output.println(s"hello ${num}")
                            output.println(s"hi ${num}")
                            output.println(s"hi ${num}")
                            Thread.sleep(1000)
                        }
                    }
                }
            }.start()
        }
    }
}
