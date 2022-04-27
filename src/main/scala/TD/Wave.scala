package TD
import scala.math._
import scala.collection.mutable.Buffer

class Wave(val goldReward: Int, var index: Int = 1) {
  def number = index
  val waves = Map(
  1 -> (50),
  2 -> (100),
    3 -> (120),
    4-> (145),
    5-> (170)
  )
  def enemies(): Buffer[Enemy] = (0 to waves(index)).map(a => new Enemy()).toBuffer
  def next = index += 1
  def load(wave: Int) = index = wave
  def reward = goldReward * pow(1.1, index).toInt
}
