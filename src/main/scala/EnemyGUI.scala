import TD.Enemy

import java.awt.Color
import scala.swing.Graphics2D

object EnemyGUI {

  def draw(enemy: Enemy, g: Graphics2D): Unit = {
    val xpos = math.round(enemy.pos.x).toInt
    val ypos = math.round(enemy.pos.y).toInt
    g.setColor(Color.BLACK)
    g.fillRect(xpos - (10 / 2) - 1, ypos - (10 / 2) - 1, 12, 12)
    g.setColor(Color.ORANGE)
    g.fillRect(xpos - (10 / 2), ypos - (10 / 2), 10, 10)

    if (enemy.hp < enemy.fullhp) {
      g.setColor(Color.GREEN)
      g.fillRect(xpos - (5), ypos - (5) - 5, (10 * ((enemy.hp * 1.0) / enemy.fullhp)).toInt, 3)
    }
  }
}
