package net.artcoder.armada.ui


import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage
import net.artcoder.armada.Player

fun main(args: Array<String>) {
    Application.launch(ArmadaApp::class.java, *args)
}


class ArmadaApp : Application(), GameStarter, GameResultAnnouncer {
    var stage: Stage? = null

    override fun start(stage: Stage) {
        this.stage = stage
        stage.scene = Scene(SetupBoardView(this))
        stage.show()
    }

    override fun startGame(player: Player) {
        stage?.scene = Scene(MatchView(player, RandomPlayer.create(), this))
        stage?.show()
    }

    override fun announceLoser() {
        val popup = Stage()
        popup.initModality(Modality.APPLICATION_MODAL)
        popup.initOwner(stage)
        val dialogVbox = VBox()
        dialogVbox.getChildren().add(Text ("You lost!"))
        val dialogScene = Scene(dialogVbox, 300.0, 200.0)
        popup.setScene(dialogScene)
        popup.show()
    }

    override fun announceWinner() {
        val popup = Stage()
        popup.initModality(Modality.APPLICATION_MODAL)
        popup.initOwner(stage)
        val dialogVbox = VBox()
        dialogVbox.getChildren().add(Text("You won!"))
        val dialogScene = Scene(dialogVbox, 300.0, 200.0)
        popup.setScene(dialogScene)
        popup.show()
    }

}
