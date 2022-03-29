package TD

// eri tornit luokiteltuna tapausluokkina
trait tower
case object Teekkari extends tower
case object Humanisti extends tower
case object Kylteri extends tower
case object TietoTeekkari extends tower
case object Astrologi extends tower
  // torneja kuvaava olio
object tower {
  implicit class attribytes(val tower: tower) extends AnyVal {
    // tornien hinnat
    def cost = tower match {
      case Teekkari => 150
      case Humanisti => 10
      case Kylteri => 100
      case TietoTeekkari => 1000
      case Astrologi => 50
    }
// Attack luokka kuvaa minkä tyyppistä hyökkäystä eri tornit käyttävät
    def attack = tower match {
      case Teekkari => 1
      case Humanisti => 1
      case Kylteri => 0
      case TietoTeekkari =>3
      case Astrologi => 2
    }
  }
}