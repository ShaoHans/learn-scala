package advanced

object ImplicitParameterDemo {
  def main(args: Array[String]): Unit = {
    implicit val name = "lucy"
    implicit val age = 18

    // 编译器会查找隐式定义的变量
    test
    // 也可以手动传入参数值
    test("tom", 22)
  }

  def test(implicit name: String, age: Int): Unit = {
    println(s"$name,$age")
  }
}

