package TD

import play.api.libs.json._
import java.nio.file.Paths
import java.io.File
import java.io.FileWriter
import java.io.BufferedWriter

import scala.util.control.NonFatal



class SaverLoader (val game: Game) {
  def save() = {
    val JSON: JsValue = Json.obj(
      "wave" -> JsNumber(game.wave.index),
      "gold" -> JsNumber(game.overall.gold),
      "hp" -> JsNumber(game.overall.hp),
      "towers" -> game.towersBuffer.map(tower =>
        Json.obj(
          "type" -> tower.Type,
          "dmg" -> tower.dmg,
          "range" -> tower.range,
          "pos" -> Json.obj("x" -> tower.pos.x, "y" -> tower.pos.y)
        )
      )
    )
    val JSONsave = Json.prettyPrint(JSON)
    println("Save starts here")
    println(JSONsave)
    println("Save ends here")

    val File = new File(Paths.get(".").toAbsolutePath + "/save_state.json")
    val writer = new BufferedWriter(new FileWriter(File))
    writer.write(JSONsave)
    writer.newLine()
    writer.flush()
    writer.close()
  }
  def load(): JsValue = {
    println("Load game")
    val File = scala.io.Source.fromFile(Paths.get(".").toAbsolutePath + "/save_state.json")
    val JSONsavestr = try File.getLines.mkString("\n") finally File.close()
    val AsJSON = Json.parse(JSONsavestr)
    println(AsJSON)
    AsJSON
  }
  def loadConfig() = {
    try {
    val File = scala.io.Source.fromFile(Paths.get(".").toAbsolutePath + "/conf.json")
    val JSONsavestr = try File.getLines.mkString("\n") finally File.close()
    val AsJSON: JsValue = Json.parse(JSONsavestr)
    println(AsJSON)
    AsJSON
    }
    catch {
      case NonFatal(e) => {
        println("Error with configuration")
        Json.obj()
      }
  }
  }
}