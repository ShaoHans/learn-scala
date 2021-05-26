package fordemo

object ForDemo {


  def main(args: Array[String]): Unit = {
    val intArr: Range.Inclusive = 1 to 10

    for (i <- intArr) {
      print(i + "\t")
    }
    println("---------------------")

    // to与until的区别：to包含10，until不包含10
    for (i <- 1 until 10 if i % 2 == 0) {
      print(i + "\t")
    }

    println("---------------------")

    // 双层循环
//    for (i <- 1 until 10; j <- 1 until 10; if j <= i) {
    for (i <- 1 until 10; j <- 1 to i) {
      print(s"$i * $j = ${i * j}\t")
      if (j == i) println()
    }

  }

}
