package net.artcoder.armada.ui

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

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
        stage?.scene = Scene(MatchView(eventBus))
        stage?.show()
    }
}

