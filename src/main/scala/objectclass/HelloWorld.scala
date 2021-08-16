package objectclass

// object相当于static，单例，不允许被new
object HelloWorld {
  println("main start")
  private val person = new Person("lili", "女")
  def main(args: Array[String]): Unit = {
    println("hello world")
    person.printAge()
  }
  println("main end")
}
// scala编译器很人性化，通过反编译工具可以看到类中定义的变量和打印语句会被scala编译器写入类的构造函数中
// 类名构造器中的参数就是类的成员属性，默认是val常量类型，且是private，
// 但可以通过var声明为变量类型：class Person(var name:String)，只有在类名构造器的参数列表中使用var，普通方法的参数列表不允许使用var声明变量，只能定义为val常量
class Person(name: String) {
  var age = 18
  var gender = "男"
  println(s"person begin")
  def this(name: String, gender: String) {
    // 必须先调用默认构造器
    this(name)
    this.gender = gender
    // 以下代码报错，因为name在类名构造器中定义，属于常量类型，不允许赋值
    //this.name = "张三"
  }
  def printAge(): Unit = {
    println(s"age=$age,name=$name,gender=$gender")
  }
  // 普通方法的参数列表不允许使用var声明变量，只能定义为val常量
  //  def setAge(var age:Int){
  //  }
  def setAge(age: Int): Unit = {
    this.age = age
  }
  println(s"person end")
}
