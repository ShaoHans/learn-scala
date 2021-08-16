package advanced

abstract class Notification

case class Email(email: String, title: String, body: String) extends Notification

case class Sms(mobile: String, content: String) extends Notification

object CaseCalssDemo {
  def main(args: Array[String]): Unit = {
    val email = Email("xxxx@sina.com", "注册成功", "感谢注册sina")
    val sms = Sms("18232222222", "恭喜您喜提特斯拉一辆")
    println(showNotification(email))
    println(showNotification(sms))

  }

  def showNotification(notification: Notification): String = {
    notification match {
      case Email(email, title, body) => s"收到来自${email}的邮件，标题：${title}，内容：${body}"
      case Sms(mobile, content) => s"收到来自${mobile}的短信，内容是：${content}"
      case _ => s"nothing"
    }
  }
}
