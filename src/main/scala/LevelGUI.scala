import TD._

import java.awt.Color
import scala.swing.Graphics2D

object Level  {
  def draw(level: Vector[Vector[Int]], g: Graphics2D, width: Int, height: Int) = {
    var x= 0
    var y = 0
    level.foreach(a => {
      a.foreach(b => {
        g.setColor(if (b == 0) Color.GRAY  else Color.GREEN)
        g.fillRect(x, y, Numbers.tileWidth, Numbers.tileHeight)
        x += Numbers.tileWidth
      })
      x = 0
      y += Numbers.tileHeight
    })
  }
}
