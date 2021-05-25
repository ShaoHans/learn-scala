package funcdemo

import java.util.Date

object FuncDemo {

  def main(args: Array[String]): Unit = {
    // 如果函数不想立马执行，可以在后面加一个下划线
    var test = test00 _

    println("--------------------")
    println(test01(10))
    println(test02("111"))

    // 匿名函数声明
    var add: (Int, Int) => Int = (a: Int, b: Int) => {
      a + b
    }
    println(add(2, 3))

    // 可以去掉函数类型，采用推断
    var sub = (a: Int, b: Int) => {
      a - b
    }
    println(sub(1, 10));

    // 嵌套函数
    def test03(s: String): Unit = {
      // 作用域：内嵌函数能访问父函数的变量
      def test01(): Unit = {
        println(s)
      }

      test01()
    }

    test03("嵌套函数")

    // 偏应用函数
    var info = log(_: Date, "info", _: String)
    info(new Date(), "ok")
    var error = log(_: Date, tp = "error", _: String)
    error(new Date(), "server exception")

    // 可变参数函数
    test04(1)
    test04(2, 3, 4)

    // 函数作为参数
    calc(2, 3, (a: Int, b: Int) => {
      a + b
    })

    calc(2, 1, (a: Int, b: Int) => {
      a / b
    })

    // 以上写法可以使用编译器的语法糖简写成如下，第一个下划线_代表第一个参数，第二个下划线代表第二个参数
    calc(2, 11, _ * _)

    calc(3, 4, opFactory("+"))
    calc(3, 4, opFactory("-"))
    calc(3, 4, opFactory("*"))
    calc(3, 4, opFactory("/"))

    test05(1, 2, 3)("a", "b", "c")
  }

  def test00(): Unit ={
    println("Hello Scala")
  }

  // 没有返回值的函数，Unit打印出来就是一对小括号()
  def test01(i: Int): Unit = {
    // i=i*10 // 函数的参数列表是val常量类型，在函数体中不允许修改其值
    println(i)
  }

  // 有返回值的函数，可以不用写return关键字
  def test02(s: String): String = {
    1 + 1
    s"Hello,$s"
  }

  def log(date: Date, tp: String, msg: String): Unit = {
    println(s"$date,$tp,$msg")
  }

  // 可变参数
  def test04(a: Int*): Unit = {
    /*for (i <- a) {
      println(i)
    }*/

    // foreach方法接受一个函数参数
    /*a.foreach(i => {
      println(i)
    })*/
    a.foreach(println)
  }

  // 函数作为参数
  def calc(a: Int, b: Int, op: (Int, Int) => Int): Unit = {
    println(op(a, b))
  }

  // 函数作为返回值
  def opFactory(op: String): (Int, Int) => Int = {
    if (op.equals("+")) {
      (a: Int, b: Int) => a + b
    } else if (op.equals("-")) {
      (a: Int, b: Int) => a - b
    } else if (op.equals("*")) {
      (a: Int, b: Int) => a * b
    } else if (op.equals("/")) {
      (a: Int, b: Int) => a / b
    } else {
      (a: Int, b: Int) => a % b
    }
  }

  // 柯里化函数：把函数的参数列表分割成一个一个的参数传递
  def test05(a: Int*)(s: String*): Unit = {
    a.foreach(println)
    s.foreach(println)
  }
}
