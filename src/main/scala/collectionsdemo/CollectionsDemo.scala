package collectionsdemo

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object CollectionsDemo {

  def main(args: Array[String]): Unit = {
    println("----------数组--------------")
    testArray()
    println("----------不可变list---------")
    testList()
    println("----------可变list----------")
    testListBuffer()
    println("----------set--------------")
    testSet()
    println("----------tuple--------------")
    testTuple()
    println("----------map--------------")
    testMap()
    println("----------链式--------------")
    testStream()
    println("----------迭代器--------------")
    testIter()
    println("----------SortedSet--------------")
    testSortedSet()
  }

  // 数组
  def testArray(): Unit = {
    // 定义整型数组，Scala中[]表示泛型，取数组下标值使用()
    val arr = Array[Int](1, 2, 3)
    arr(1) = 22
    //println(arr(1))
    arr.foreach(println)
  }

  // (immutable)不可变list
  def testList(): Unit = {
    val list = List(1, 2, 3, "d")
    list.foreach(println)
    val list2 = List[Int](1, 2, 3)
    list2.foreach(println)
  }

  // (mutable)可变list
  def testListBuffer(): Unit = {
    val list = ListBuffer(1, 2)
    list.insert(0, -1)
    list.+=(3)
    list.+=(4)

    var list2 = ListBuffer[Any]("aa", "bb")
    // ++（相当于 :++） 合并两个集合，把list2追加到list中，返回一个新集合
    //var list3 = list.++(list2)
    //var list3 = list.:++(list2)
    //list3.foreach(println)

    // ++: 把list追加到list2中
    var list4 = list.++:(list2)
    list4.foreach(println)

  }

  def testSet(): Unit = {
    // 不可变Set
    val s1 = Set(1, 4, 1, 2, 4, 3)
    s1.foreach(println)

    // 可变Set
    val s2 = scala.collection.mutable.Set(2, "a")
    s2.add(3)
    s2.foreach(println)
  }

  // 元组
  def testTuple(): Unit = {
    val t2 = Tuple2(11, "sss")
    val t5 = (11, 22, 33, "666", (a: Int) => a * a)
    println(t5._5)
    println(t5._5(11))

    val iterator = t5.productIterator
    while (iterator.hasNext) {
      println(iterator.next())
    }
  }

  def testMap(): Unit = {
    // 不可变Map
    val m1 = Map(("k1", 11), "k2" -> "33", ("k3", (a: Int) => a * a))
    m1.foreach(item => {
      println(s"${item._1}=${item._2}")
    })

    println(m1.get("k1"))
    println(m1.getOrElse("kk", 0))

    // 可变Map
    val m2: scala.collection.mutable.Map[String, Any] = scala.collection.mutable.Map(("k1", 11))
    m2.put("k2", "33")
  }

  def testStream(): Unit = {
    val list = List(1, 2, 3, 4)
    list.map(i => i * i).map(_ - 10).foreach(println)

    val words = List("hello,c#", "tom,lili", "hello,beijing")
    words.flatMap(w => w.split(",")).foreach(println)
    //words.flatMap(w => w.split(",")).map(w => Tuple2(w, 1)).foreach(println)
    words.flatMap(w => w.split(",")).map((_, 1)).foreach(println)
  }

  def testIter(): Unit = {
    // 迭代器模式，迭代器里不存数据，只存一个类似指针的对象，节省内存
    val words = List("hello,c#", "tom,lili", "hello,beijing")
    val iter = words.iterator
    iter.flatMap(w => w.split(",")).map((_, 1)).foreach(println)
  }

  def testSortedSet(): Unit ={
    implicit val o = new Ordering[(Int,Int)] {
      override def compare(x: (Int, Int), y: (Int, Int)): Int = y._2.compare(x._2)
    }
    val tuples = scala.collection.immutable.SortedSet((4, 10), (3, 15), (1, 35), (13, 20), (1, 32))
    println(tuples.head)
    tuples.foreach(println)
  }
}
