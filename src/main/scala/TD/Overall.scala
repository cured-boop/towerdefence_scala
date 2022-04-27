package TD
import play.api.libs.json._

class Overall (var hp: Int, var gold: Int) { //kuvaa sen hetkistä pelitilannetta
  def damage() = hp -= 1
  def bought(tower: Tower) = this.gold -= tower.cost
  def buyable(tower: Tower) =  this.gold >= tower.cost
  def isOver = hp > 0
  def money(amount: Int) = gold += amount
  def loadStatus(json: JsValue) = {
    gold = (json \ "gold").as[Int]
    hp = (json \ "hp").as[Int]
  }
}

