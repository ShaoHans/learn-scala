package advanced

import java.util

object ImplicitClassDemo {
  def main(args: Array[String]): Unit = {
    val list = new util.LinkedList[Int]
    list.add(1)
    list.add(2)
    list.add(3)
    list.foreach(println)
  }

  // 隐式转换类
  implicit class LinkedListExtensions[T](list: util.LinkedList[T]) {
    def foreach(f: T => Unit): Unit = {
      val iter = list.iterator()
      while (iter.hasNext) {
        f(iter.next())
      }
    }
  }

}

