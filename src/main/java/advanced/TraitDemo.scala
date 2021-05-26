package advanced

trait Fly {
  def fly(): Unit = {
    println("flying...")
  }
}

trait Walk {
  def walk(): Unit = {
    println("walking...")
  }

  def swim()
}

class Duck extends Fly with Walk {
  def say(): Unit = {
    println("gagagag....")
  }

  override def swim(): Unit = {
    println("feet swim")
  }
}

object TraitDemo {
  def main(args: Array[String]): Unit = {
    val duck = new Duck()
    duck.fly()
    duck.walk()
    duck.say()
    duck.swim()
  }
}
