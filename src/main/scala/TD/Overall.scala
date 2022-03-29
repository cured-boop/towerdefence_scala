package TD

case class Overall (level:Level, hp: Int, gold: Int) //kuvaa sen hetkistä pelitilannetta

object Overall {

  implicit class attribyte(val overall: Overall) extends AnyVal {
    def level = overall.level
    def life = if (overall.hp >= 0) overall.hp else 0
    def gold = if(overall.gold >= 0) overall.gold else 0
  }
}
