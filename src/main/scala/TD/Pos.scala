package TD

class Pos( firstx: Int, firsty: Int) {
  var x: Int = firstx.toInt
  var y: Int = firsty.toInt
  def addx(amount: Int): Unit = this.x += amount
  def addy(amount: Int):Unit = this.y += amount
  override def toString() = "(" + this.x + "," + this.y + ")"
}
class PosD( firstx: Double, firsty: Double) {
  var x: Double = firstx
  var y: Double = firsty
  def addx(amount:Double): Unit = this.x += amount
  def addy(amount:Double): Unit = this.y += amount
  override def toString() = "(" + this.x + "," + this.y + ")"
}
