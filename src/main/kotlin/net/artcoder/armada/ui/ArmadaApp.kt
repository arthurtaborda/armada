package net.artcoder.armada.ui


import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import net.artcoder.armada.Player

fun main(args: Array<String>) {
    Application.launch(ArmadaApp::class.java, *args)
}


class ArmadaApp : Application(), GameStarter {

    var stage: Stage? = null

    override fun start(stage: Stage) {
        this.stage = stage
        stage.scene = Scene(SetupBoardView(this))
        stage.show()
    }

    override fun startGame(player: Player) {
        stage?.scene = Scene(MatchView(player, RandomPlayer.create()))
        stage?.show()
    }

}
