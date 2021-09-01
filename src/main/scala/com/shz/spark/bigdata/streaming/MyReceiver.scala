package com.shz.spark.bigdata.streaming

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

class MyReceiver(host: String, port: Int) extends Receiver[String](StorageLevel.MEMORY_AND_DISK_SER_2) {
    override def onStart(): Unit = {
        new Thread(){
            override def run(): Unit = {
                exec()
            }
        }.start()
    }

    private def exec(): Unit ={
        val server = new Socket(host, port)
        val reader = new BufferedReader(new InputStreamReader(server.getInputStream))
        var line: String = reader.readLine()
        while(!isStopped()  &&  line != null ){
            store(line)
            line  = reader.readLine()
        }
    }

    override def onStop(): Unit = {

    }
}
