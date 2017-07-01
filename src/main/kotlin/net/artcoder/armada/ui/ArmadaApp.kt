package net.artcoder.armada.ui

import javafx.application.Application
import javafx.stage.Stage
import tornadofx.*


class ArmadaApp : App(SetupBoardView::class) {
    override fun start(stage: Stage) {
        stage.isResizable = false
        stage.width = 650.0
        stage.height = 350.0
        super.start(stage)
    }
}


fun main(args: Array<String>) {
    Application.launch(ArmadaApp::class.java, *args)
}