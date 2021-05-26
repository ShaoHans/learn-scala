package advanced

import java.util

object ImplicitFunctionDemo {
  def main(args: Array[String]): Unit = {
    val list = new util.LinkedList[Int]
    list.add(1)
    list.add(2)
    list.add(3)
    list.foreach(println)
  }

  // 隐式转换方法，类似于C#中的扩展方法
  implicit def anyName[T](list: util.LinkedList[T]): LinkedListExtensions[T] = {
    new LinkedListExtensions(list)
  }
}

class LinkedListExtensions[T](list: util.LinkedList[T]) {
  def foreach(f: T => Unit): Unit = {
    val iter = list.descendingIterator()
    while (iter.hasNext) {
      f(iter.next())
    }
  }
}
