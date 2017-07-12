package net.artcoder.armada.ui

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import net.artcoder.armada.*

fun main(args: Array<String>) {
    Application.launch(ArmadaApp::class.java, *args)
}


class ArmadaApp : Application() {
    private val ships = intArrayOf(2, 2, 3, 3, 4, 5)

    private var stage: Stage? = null
    private val eventBus = EventBus()

    override fun start(stage: Stage) {
        val setupBoard = SetupBoard(ships)

        eventBus.register(this)

        this.stage = stage
        stage.scene = Scene(SetupBoardView(eventBus, setupBoard))
        stage.show()
    }

    @Subscribe
    fun handle(event: GameStartedEvent) {
        val boardGenerator = BoardGenerator(ships, RandomPointGenerator())
        val match = BattleshipMatch(eventBus, event.board, boardGenerator.randomBoard())
        stage?.scene = Scene(MatchView(eventBus, match))
        stage?.show()
    }
}

