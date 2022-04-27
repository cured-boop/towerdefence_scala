package TD
import TD._


class Enemy(speed: Double = 1.0, val fullhp: Int = 100) {
  val game = new Game
  var pos: PosD = new PosD(0,0)
  var hp: Int  = fullhp
  var toDirection = Direction.none
  var onRoad: Int = 0
  var toFinish: Int = 0

  def move(x: Int, y: Int) = {
    this.pos.addx(x * this.speed)
    this.pos.addy(y * this.speed)
    toFinish -= (math.abs(x) + math.abs(y))
  }

  def getHit(dmg: Int) = hp -= dmg

  def madeIt = (pos.y > Numbers.height || pos.y < 0) || (pos.x > Numbers.width || pos.x < 0)
}
object noTarget extends Enemy {
  override def getHit(dmg:Int) = hp-= dmg
}














/*
trait Enemy
case object beer extends Enemy //heikoin vihollinen
case object long extends Enemy //toinen vihollinen
case object booze extends Enemy //kolmas vihollinen

case class beer(text: String) extends Enemy {
  val Speed = this.speed
  val Kuva = this.kuva
  val ToDirection = this.toDirection(Kuva)
  val Suunta = this.suunta
  }
object Enemy {

  var currentDir = "right"
  implicit class attribyte(val enemy: Enemy) extends AnyVal {
  def speed = enemy match {
    case beer => 1.0
    case long => 1.5
    case booze => 2.0
  }
   def kuva = enemy match {
     case beer => scale(new ImageView(new Image("Pictures/ekaolut.PNG")), 10, 10, 70, 50)
     case long => scale(new ImageView(new Image("Pictures/long.PNG")), 10, 10, 70, 50)
     case booze => scale(new ImageView(new Image("Pictures/booze.PNG")), 10, 10, 70, 50)
   }
    def suunta = currentDir
    def toDirection(kuva: ImageView): Unit = {
  if (kuva.x.value == 900 && kuva.y.value <= 10 ) currentDir = "down"
  else if (kuva.y.value == 600 && kuva.x.value >= 800 ) currentDir = "left"
  else if (kuva.x.value <= 100 && kuva.y.value == 600 ) currentDir = "up"
  else if (kuva.x.value <= 100 && kuva.y.value <= 200 )  currentDir = "right"
  else if (kuva.x.value >= 600 && kuva.y.value <= 200 && kuva.y.value >= 110) currentDir = "down"
  else if (kuva.x.value >= 600 && kuva.y.value <= 900 && kuva.y.value >= 800) currentDir = "left"

 }

}


 val olut = new Image("Pictures/ekaolut.PNG")
  var olutkuva = new ImageView(olut)
  olutkuva = scale(olutkuva, 10, 10, 70, 50)

}

 */
