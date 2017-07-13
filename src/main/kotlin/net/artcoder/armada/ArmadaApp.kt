package net.artcoder.armada

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
import net.artcoder.armada.bot.RandomPointGenerator
import net.artcoder.armada.bot.SmartBot
import net.artcoder.armada.core.BoardGenerator
import net.artcoder.armada.core.gui.BoardView
import net.artcoder.armada.match.*
import net.artcoder.armada.match.gui.MatchView
import net.artcoder.armada.setup.GameStartedEvent
import net.artcoder.armada.setup.SetupBoard
import net.artcoder.armada.setup.SetupBoardController
import net.artcoder.armada.setup.gui.SetupBoardView

fun main(args: Array<String>) {
    Application.launch(ArmadaApp::class.java, *args)
}


class ArmadaApp : Application() {
    private val ships = intArrayOf(2, 2, 3, 3, 4, 5)

    private var stage = Stage()
    private var eventBus = EventBus()

    override fun start(stage: Stage) {
        this.stage = stage

        newGame()
    }

    @Subscribe
    fun handle(event: GameStartedEvent) {
        val pointGenerator = RandomPointGenerator()
        val botBoard = BoardGenerator(ships, pointGenerator).randomBoard()
        val bot = SmartBot(pointGenerator)
        val match = SinglePlayMatch(eventBus, event.board, botBoard, bot)
        val playerBoardView = BoardView("player", eventBus)
        val opponentBoardView = BoardView("opponent", eventBus)
        val controller = MatchController(match, "opponent")
        val matchView = MatchView(playerBoardView, opponentBoardView)

        eventBus.register(match)
        eventBus.register(controller)
        eventBus.register(matchView)

        stage.scene = Scene(matchView)
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
        eventBus = EventBus()
        eventBus.register(this)

        val setupBoard = SetupBoard(ships)
        val boardView = BoardView(eventBus)
        val controller = SetupBoardController(eventBus, setupBoard)
        val setupBoardView = SetupBoardView(boardView, controller)

        eventBus.register(controller)
        eventBus.register(setupBoardView)

        stage.scene = Scene(setupBoardView)
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

