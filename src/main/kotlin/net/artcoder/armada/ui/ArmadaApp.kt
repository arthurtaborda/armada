package net.artcoder.armada.ui

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.image.ImageView
import javafx.stage.Stage
import net.artcoder.armada.*

fun main(args: Array<String>) {
    Application.launch(ArmadaApp::class.java, *args)
}


class ArmadaApp : Application() {
    private val ships = intArrayOf(2, 2, 3, 3, 4, 5)

    private var stage = Stage()
    private val eventBus = EventBus()

    override fun start(stage: Stage) {
        eventBus.register(this)

        this.stage = stage

        newGame()
    }

    @Subscribe
    fun handle(event: GameStartedEvent) {
        val pointGenerator = RandomPointGenerator()
        val botBoard = BoardGenerator(ships, pointGenerator).randomBoard()
        val bot = SmartBot(pointGenerator)
        val match = SinglePlayMatch(eventBus, event.board, botBoard, bot)
        eventBus.register(match)
        stage.scene = Scene(MatchView(eventBus, match))
        stage.show()
    }

    @Subscribe
    fun handle(event: PlayerWonEvent) {
        gameOverDialog("You won!", "icons/win.png")
    }

    @Subscribe
    fun handle(event: OpponentWonEvent) {
        gameOverDialog("You lost!", "icons/lose.png")
    }

    private fun newGame() {
        val setupBoard = SetupBoard(ships)
        stage.scene = Scene(SetupBoardView(eventBus, setupBoard))
        stage.show()
    }

    private fun gameOverDialog(text: String, iconPath: String) {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Game Over"
        alert.headerText = text
        alert.graphic = ImageView(javaClass.classLoader.getResource(iconPath).toString())

        val playAgainButtonType = ButtonType("Play again")
        val quitButtonType = ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE)
        alert.buttonTypes.setAll(playAgainButtonType, quitButtonType)

        val result = alert.showAndWait()

        if (result.get() == playAgainButtonType) {
            newGame()
        } else if (result.get() == quitButtonType) {
            Platform.exit()
        }
    }
}

