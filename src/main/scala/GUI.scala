import TD.{Game, Numbers}

import java.awt.Color
import java.awt.event.ActionListener
import scala.swing.event.ButtonClicked
import scala.swing.{BoxPanel, Button, Dimension, Graphics2D, Label, MainFrame, Orientation, Panel, SimpleSwingApplication}

object GUI extends SimpleSwingApplication {
  var game = new Game()

  def top = new MainFrame {

    title = "Beverage Tower Defence"
    val width = 800
    val height = 660
    minimumSize = new Dimension(width, height)
    preferredSize = new Dimension(width, height)
    maximumSize = new Dimension(width, height)

    val start = new Button("Start Game")
    val load = new Button("Load Game")
    val continue = new Button("Continue")
    val speed = new Button(">")
    val restart = new Button("Play Again")
    val exit = new Button("Exit")

    val t1 = new Button("Tower 1")
    val t2 = new Button("Tower 2")
    val t3 = new Button("Tower 3")
    val t4 = new Button("Tower 4")
    val t5 = new Button("Tower 5")
    val Testing = new Button("Testing")

    val msg = new Label("")
    val wave = new Label("Wave: " + (game.wave.index))
    val hp = new Label("   Health: " + game.overall.hp)
    val gold = new Label("   Gold: " + game.overall.gold)
    val tower = new Label("<html><br></html>")

    def labels() = {
      wave.text = ("Wave: " + (game.wave.index))
      hp.text = ("   Health: " + game.overall.hp)
      gold.text = ("   Gold: " + game.overall.gold)
      val thisTower = game.towers.wrap(game.choosingTower)
      tower.text = if (thisTower.isEmpty) "<html><br><br><br><br><br><br></html>" else (
        "<html><br>" +
          "Tower " + thisTower.get.Type +
          "<br><br>Cost: " + thisTower.get.cost + " gold" +
          "<br>Damage: " + thisTower.get.dmg +
          "<br>Range: " + thisTower.get.range +
          "<br></html>"
        )
    }

    def message(text: String) = {
      msg.text = "   " + text
    }

    val mainMenu = new BoxPanel(Orientation.Vertical) {
      val startButtons: BoxPanel = new BoxPanel(Orientation.Horizontal) {
        contents += start
        contents += load
        contents += exit
      }
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label("<html><br><br><br><br><h1>Beverage Tower Defence</h1><br><br><br><br></html>")
      }
      contents += startButtons
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += msg
      }
    }

    class Shop extends Panel {

      override def paintComponent(g: Graphics2D): Unit = {
        g.clearRect(0, 0, Numbers.width, Numbers.height)
        Level.draw(game.level.get, g, Numbers.width, Numbers.height)

        g.setColor(Color.YELLOW)
        game.enemiesBuffer.foreach(a => {
          EnemyGUI.draw(a, g)
        })

        g.setColor(Color.MAGENTA)
        game.towersBuffer.foreach(b => {
          TowerGUI.draw(b, g)
        })
      }
    }

    val screen: BoxPanel = new BoxPanel(Orientation.Vertical) {
      val screenText: BoxPanel = new BoxPanel(Orientation.Horizontal) {
        contents += wave
        contents += hp
        contents += gold
        contents += msg
      }
      val screenButton = new BoxPanel(Orientation.Horizontal) {
        contents += t1
        contents += t2
        contents += t3
        contents += t4
        contents += t5
        contents += Testing
        contents += speed
      }
      val aboutTower = new BoxPanel(Orientation.Horizontal) {
        contents += tower
      }
      val info = new BoxPanel(Orientation.Horizontal) {
        contents += screenButton
        contents += aboutTower
      }
      contents += screenText
      contents += new Shop()
      contents += info
    }
    val gg = new Label("")

    def gege() = gg.text = ("<html><br><br><br><br><h1> GG! </h1><br><br><h3>You made it to wave " + game.wave.number + "</h3><br><br></html>")

    val winText = new Label("<html><br><br><br><br><h1>Game is won</h1><br><br></html>")
    val winInfo = new Label("")

    def winning: Unit = winInfo.text = ("<html><h3>You defeated wave " + game.wave + ", with " + game.overall.hp + " health</h3><br><br></html>")

    val ggScreen = new BoxPanel(Orientation.Vertical) {
      val ggButtons = new BoxPanel(Orientation.Horizontal) {
        contents += restart
        contents += exit
      }
      val ggLabel = new BoxPanel(Orientation.Horizontal) {
        contents += gg
      }
      contents += ggLabel
      contents += ggButtons
    }
    val won = new BoxPanel(Orientation.Horizontal) {
      val wonButtons = new BoxPanel(Orientation.Horizontal) {
        contents += restart
        contents += continue
        contents += exit
      }
      val wonLabel = new BoxPanel(Orientation.Horizontal) {
        contents += winText
      }
      val wonInfoBox = new BoxPanel(Orientation.Horizontal) {
        contents += winInfo
      }
      contents += wonLabel
      contents += wonButtons
      contents += wonInfoBox
    }
    val window: BoxPanel = new BoxPanel(Orientation.Vertical) {
      contents += mainMenu
    }

    def gameStart() = {
      game.start()
      window.contents -= mainMenu
      window.contents += screen
      window.revalidate()
      window.repaint()
      top.repaint()
      timer.start()
    }

    def loadGame() = {
      val loaded = game.loadGame()
      if (loaded) {
        gameStart()
      }
      else message("Error while loading")
    }

    def restarting() = {
      if (game.lost) window.contents -= ggScreen
      else window.contents -= won
      game = new Game()
      gameStart()
    }

    def continueGame() = {
      window.contents -= won
      window.contents += screen
      window.revalidate()
      window.repaint()
      top.repaint()
      timer.start()
    }

    this.listenTo(start)
    this.listenTo(load)
    this.listenTo(exit)
    this.listenTo(restart)
    this.listenTo(continue)
    this.listenTo(t1)
    this.listenTo(t2)
    this.listenTo(t3)
    this.listenTo(t4)
    this.listenTo(t5)
    this.listenTo(Testing)
    this.listenTo(speed)
    this.listenTo(screen.mouse.clicks)

    this.reactions += {
      case btnClicked: ButtonClicked => {
        val button = btnClicked.source
        val text = button.text
        text match {
          case "Start Game" => gameStart()
          case "Load Game" => loadGame()
          case "Play Again" => restarting()
          case "Continue" => continueGame()
          case "Tower 1" => game.towerWasChosen(1)
          case "Tower 2" => game.towerWasChosen(2)
          case "Tower 3" => game.towerWasChosen(3)
          case "Tower 4" => game.towerWasChosen(4)
          case "Tower 5" => game.towerWasChosen(5)
          case "Special" => println("Special")
          case ">" => {
            println("Speed up")
            timer.stop()
            timer = new javax.swing.Timer(8, listener)
            timer.start()
            speed.text = ">>"
          }
          case ">>" => {
            println("Speed down")
            timer.stop()
            timer = new javax.swing.Timer(16, listener)
            timer.start()
            speed.text = ">"
          }
          case "Exit" => System.exit(0)
        }
      }
      case scala.swing.event.MousePressed(src, point, _, _, _) => game.onClick(src, point)
    }
    contents = window
    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) = {
        screen.revalidate()
        screen.repaint()
        message(game.messagePrint)
        labels()

        game.onTick()
        if (game.won) {
          (e.getSource.asInstanceOf[javax.swing.Timer]).stop()
          winning
          window.contents -= screen
          window.contents += won
          window.revalidate()
          window.repaint()
          top.repaint()
        }
        else if (game.lost) {
          (e.getSource.asInstanceOf[javax.swing.Timer]).stop()
          gege()
          window.contents -= screen
          window.contents += ggScreen
          window.revalidate()
          window.repaint()
          top.repaint()
        }
      }
    }
    var timer = new javax.swing.Timer(16, listener)
  }
}
