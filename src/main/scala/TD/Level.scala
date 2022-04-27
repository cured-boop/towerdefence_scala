package TD
import scala.swing.Point

class Level(private val layout: Vector[Vector[Int]], val width: Int = 1000, val height: Int = 1000) {
  def get = layout
  val road = {
    var end = 1
    var road = Array[Pos]()
    layout.zipWithIndex.foreach(a => {
      val row = a._1
      val index = a._2
      val spot = row.zipWithIndex.filter(b => b._1 == 1).map(c => new Pos(c._2 + 1, index +1 ))
      if (spot.nonEmpty) {
        road = road ++ (if (spot(0).x == end) spot else spot.reverse)
        end = spot.last.x
      }
    })
      road
  }
  def pointOnRoad(cursor: Point) = {
    val test = road.map(spot => new Pos((spot.x - 1) * Numbers.tiwi, (spot.y - 1) * Numbers.tihe)).find(spot => cursor.x > spot.x && cursor.x < spot.x + Numbers.tiwi && cursor.y > spot.y && cursor.y < spot.y + Numbers.tihe)
    test.isEmpty
  }
  def oob(cursor: Point) = cursor.x < 0 || cursor.x > Numbers.width || cursor.y < 0 || cursor.y > Numbers.height

}



/*
// vapaamuotoinen tasoa kuvaava nelikulmio
trait Rectangle[A] {
  def y(a: A): Int
  def x(a: A): Int
  def width(a: A): Int
  def height(a: A): Int
}
// yksittäinen ruutu
trait Tile
case object road extends Tile //vihollisten kulkureitti
case object spot extends Tile //torneja varten varattu ruutu
case object start extends Tile //vihollisten "spawn"
case object goal extends Tile //vihollisten maali



// ruutu olio
object Tile {
  implicit class attribyte(val tile: Tile) extends AnyVal {
    def towerable = tile match {
      case spot => true
      case _ => false
    }
  }
}
// ruutua vastaava sijainti
case class Pos(x: Int, y:Int)

object Pos {
  implicit class attribyte(val pos: Pos) extends AnyVal {
    def withIn[R](r: R)(implicit ins: Rectangle[R]) = { // tarkoituksena tässä avata ruudun ja tason suhdetta.
      val Pos(x,y) = pos
      val top = ins.y(r)
      val left = ins.x(r)
      val right = y + ins.width(r)
      val bottom = y + ins.height(r)
      x >= left && x < right && y >= top && y < bottom
    }
  }
}

case class order(pos:Pos, tower: tower) //apuluokka torneihin käsiksi pääsemiseen

case class Level(width: Int, height: Int, tiles: Map[Pos, Tile], towers: Map[Pos, tower]) //tasoa kuvaava konkreettinen osuus

  object Level {
  implicit lazy val levelLayout = new Rectangle[Level] { //otin tässä mallia ja ideana oli ilmeisesti eri tasojen välillä siirtymisen helpottaminen
    def y(l: Level) = 0
    def x(l: Level) = 0
    def width(l: Level) = if (l.width >= 0) l.width else -l.width
    def height (l: Level) = if (l.height >= 0) l.height else -l.height
  }
    implicit class attribyte(val level: Level) extends AnyVal {
      def tile(p: Pos): Option[Tile] = {
        if (p.withIn(level)) Some(level.tiles.getOrElse(p, road))
        else None
      }
      def tower(p: Pos): Option[tower] = {
        if (p.withIn(level)) level.towers.get(p)
        else None
      }
    def y(implicit r: Rectangle[Level]) = r.y(level)
    def x(implicit r: Rectangle[Level]) = r.x(level)
    def width(implicit r: Rectangle[Level]) = r.width(level)
    def height(implicit r: Rectangle[Level]) = r.height(level)
    }
  }

 */