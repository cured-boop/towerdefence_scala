package TD

import play.api.libs.json._
import TD.Numbers
import scala.swing._
import scala.collection.mutable.Buffer
import scala.util.control.NonFatal

class Game() {
  private val reader = new SaverLoader(this)



  val (
    hp,
    gold,
    reward,
    pledge,
    jsontowers,
    leveler
  ) = loadConfig()

  val towers: towersToSaveState = new towersToSaveState(this, jsontowers)
  val overall: Overall = new Overall(hp, gold)
  val wave: Wave = new Wave(reward, 1)
  val level: Level = new Level(leveler)
  var time = 1
  var over = false
  var choosingTower = 0
  var messagePrint = ""
  var messageTime = 0
  var enemiesBuffer = Buffer[Enemy]()
  var towersBuffer = Buffer[Tower]()

  def loadConfig() = {
    val JSON = reader.loadConfig()
    val hp:Int = (JSON \ "hp").as[Int]
    val gold: Int = (JSON \ "gold").as[Int]
    val reward: Int = (JSON \"reward").as[Int]
    val pledge: Int = (JSON \ "pledge").as[Int]
    var jsontowers: List[JsValue] = (JSON \ "towers").asOpt[List[JsValue]].get
    var leveler: Vector[Vector[Int]] = (JSON \ "level").as[Vector[Vector[Int]]]
    (
      hp,
      gold,
      reward,
      pledge,
      jsontowers,
      leveler
    )
  }
  def loadGame():Boolean = {
    try {
    val JSON = reader.load()
    overall.loadStatus(JSON)
    wave.load((JSON \ "wave").as[Int])
    val towerList:List[JsValue]  = (JSON \ "towers").as[List[JsValue]]
    towersBuffer = towerList.map(tower => {
      val Type = (tower \ "type").as[String]
      val pos = (tower \ "pos").as[Map[String, Int]]
      (Type match {
        case "1" => towers.wrap(1, new Pos(pos("x"), pos("y")))
        case "2" => towers.wrap(2, new Pos(pos("x"), pos("y")))
        case "3" => towers.wrap(3, new Pos(pos("x"), pos("y")))
        case "4" => towers.wrap(4, new Pos(pos("x"), pos("y")))
        case "5" => towers.wrap(5, new Pos(pos("x"), pos("y")))
      }).get
    }).toBuffer
    println(towersBuffer)
      true
    } catch {
      case NonFatal(err) => {
      println(err)
        false
      }
    }
  }
  def onTick() = {
    enemyDies()
    moveEnemies()
    timeToAttack(time)
    if (levelOver) afterLevel()
    time += 1
    messageTime += 1
    if (messageTime >= 200) removeMessage()
  }
  def save(): Unit = {
    reader.save()
  }
  def start() = {
    enemiesBuffer = wave.enemies()
    engage()
    println("Start Game")
  }
  def engage() = {
    val beginning = level.road(0)
    val roadLength = level.road.map(a => a.x * Numbers.tiwi).sum
    enemiesBuffer.zipWithIndex.foreach(a => {
      val enemy = a._1
      val distance = 50
      val toDir = this.toDirection(enemy)
      if (toDir == Direction.right)  enemy.move((-a._2 * 6) - distance, ((beginning.y - 1) * Numbers.tihe) + (Numbers.tihe/2))
      else if (toDir == Direction.down) enemy.move(((beginning.x -1) * Numbers.tiwi) + (Numbers.tiwi/2), (-a._2 * 6) - distance)
      enemy.toDirection = toDir
      enemy.toFinish = roadLength + ((a._2 * 6) + distance)
    })
  }
  private def toDirection(enemy: Enemy) = {
    val beginning = level.road(0)
    val after = level.road(1)
    (after.x - beginning.x, after.y - beginning.y) match {
      case (1, 0) => Direction.right
      case (-1, 0) => Direction.left
      case (0, 1) => Direction.down
      case (0, -1) => Direction.up

    }
  }
  def lastTile(enemy: Enemy) = level.road.length - 1 == enemy.onRoad
  def moveEnemies() = {
    val throught = Buffer[Int]()
    enemiesBuffer.zipWithIndex.foreach(a => {
      val enemy = a._1
      if (!lastTile(enemy)) {
        val now = level.road(enemy.onRoad)
        val next = level.road(enemy.onRoad + 1)
        val X = enemy.pos.x
        val Y = enemy.pos.y
        if (((X > ((now.x * Numbers.tiwi) - (Numbers.tiwi/2))) && enemy.toDirection == Direction.right) || ((X < ((now.x * Numbers.tiwi) - Numbers.tiwi/2)) && enemy.toDirection == Direction.left)) {
          if (next.y == now.y) "brple"
          else {
          if (next.y - now.y == 1) enemy.toDirection = Direction.down
          else enemy.toDirection = Direction.up
          }
          enemy.onRoad += 1
        } else if (((Y > ((now.y * Numbers.tihe) - (Numbers.tihe/2))) && enemy.toDirection == Direction.down) || ((Y < ((now.y * Numbers.tihe) - Numbers.tihe/2)) && enemy.toDirection == Direction.up)) {
          if (next.x == now.x) {
            new Pos(0, next.y - now.y)
          } else {
            if (next.x - now.x == 1) enemy.toDirection = Direction.right
            else enemy.toDirection = Direction.left
        }
          enemy.onRoad += 1
        }
        }
      else if ((level.road.length -1 == enemy.onRoad) && enemy.madeIt ) throught += a._2
      val letsgo = enemy.toDirection match {
        case Direction.left => (-1, 0)
        case Direction.right => (1, 0)
        case Direction.up => (0, -1)
        case Direction.down => (0, 1)
      }
      enemy.move(letsgo._1, letsgo._2)
      })
  throught.zipWithIndex.foreach(a => enemyDoesNotDie(a._1  - a._2))
  }
  def timeToAttack(time: Int): Unit = {
    towersBuffer.foreach(a => a.attack(time))
  }
  def enemyDoesNotDie(index: Int) = {
    enemiesBuffer.remove(index)
    overall.damage()
  }
  def enemyDies() = {
    val alive = enemiesBuffer.length
    enemiesBuffer = enemiesBuffer.filter(a => a.hp > 0)
    val dead = enemiesBuffer.length
    overall.money((alive - dead) * pledge)
  }
  def onClick(src: Component, cursor: Point) = {
    if (choosingTower != 0) {
      val tower = chooseTower(cursor)
      placeable(cursor, tower.get)
    }
  }

  def message(msg: String) = {
    messagePrint= msg
    messageTime = 0
  }
  def removeMessage() = {
    messagePrint= ""
    messageTime = 0
  }
  def won = { if(wave.index == 50 && !over)   {
    over = true
    true
  }
    else false
  }
  def lost = !overall.isOver
  def levelOver = enemiesBuffer.isEmpty
  def afterLevel() = {
    overall.money(wave.reward)
    wave.next
    enemiesBuffer = wave.enemies()
    engage()
    save()
  }
  def towerWasChosen(Type: Int) = {choosingTower = Type}

  def chooseTower(cursor: Point): Option[Tower] = towers.wrap(choosingTower, new Pos(cursor.x, cursor.y))

  def placeable(cursor: Point, tower: Tower) = {
    if (!overall.buyable(tower)) message("Insufficient gold")
    else if (!level.pointOnRoad(cursor)|| level.oob(cursor)) message("That's not where this goes")
    else {
      place(tower)
      choosingTower = 0
    }
  }
  def place(tower: Tower) = {
    towersBuffer += tower
    overall.bought(tower)
  }

}
object Direction {
  val none = 0
  val left = 1
  val right = 2
  val up = 3
  val down =4
}
