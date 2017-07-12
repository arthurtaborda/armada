package net.artcoder.armada.ui

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import net.artcoder.armada.BattleshipMatch

fun main(args: Array<String>) {
    Application.launch(ArmadaApp::class.java, *args)
}


class ArmadaApp : Application() {
    private var stage: Stage? = null
    private val eventBus = EventBus()

    override fun start(stage: Stage) {
        eventBus.register(this)

        this.stage = stage
        stage.scene = Scene(SetupBoardView(eventBus))
        stage.show()
    }

    @Subscribe
    fun handle(event: GameStartedEvent) {
        val match = BattleshipMatch(eventBus, event.board, event.board)
        stage?.scene = Scene(MatchView(eventBus, match))
        stage?.show()
    }
}

