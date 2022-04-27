import TD.Tower

import scala.swing.Graphics2D

object TowerGUI {
  def draw(tower: Tower, g: Graphics2D) = {
    g.fillRect(tower.pos.x - (30 / 2), tower.pos.y - (30), 30, 30)
    g.drawOval(tower.pos.x - tower.range, tower.pos.y - tower.range - (30 / 2), tower.range * 2, tower.range * 2)
  }
}
