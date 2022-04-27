package TD

import play.api.libs.json._

class Tower(val dmg: Int, val range: Int, val cost: Int, val Type: String, val pos: Pos, val game: Game) {
  def withinRange(enemies: Vector[Enemy]): Vector[Enemy] = enemies.filter(a => math.sqrt(math.pow(pos.x - a.pos.x, 2) + math.pow(pos.y - a.pos.y, 2)) < range).sortBy(b => b.toFinish).toVector
  def attack(time: Int) = {
    if (cooldown(time)) {
      withinRange(game.enemiesBuffer.toVector).headOption.getOrElse(noTarget).getHit(dmg)
    }
  }
  private def cooldown(time: Int) = time % (100.0 / (1.0 * 100)) < 1
}
class mainInfo (val dmg: Int, val range: Int, val cost: Int) {}

class towersToSaveState(val game: Game, JSONtower: List[JsValue]) {
  val Alltowers: Map[Int, mainInfo] = JSONtower.zipWithIndex.map(a => {
    val towerInfo = a._1
    val index = a._2
    val dm = (towerInfo \ "dmg").as[Int]
    val rang = (towerInfo \ "range").as[Int]
    val cos = (towerInfo \ "cost").as[Int]

    (index + 1, new mainInfo(dm, rang, cos))
  }).toMap
  def wrap(Type: Int, pos: Pos = new Pos(0,0)): Option[Tower] = {
    val towerConfOption = Alltowers.get(Type)
    if (towerConfOption != None) {
      val towerConf = towerConfOption.get
      Some(new Tower(towerConf.dmg, towerConf.range, towerConf.cost, Type.toString, pos, game))
    } else {
      None
    }
  }
}