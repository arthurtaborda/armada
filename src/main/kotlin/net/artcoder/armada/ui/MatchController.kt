package net.artcoder.armada.ui

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe

class MatchController(eventBus: EventBus) {

    @Subscribe fun handle(event: MouseEnteredCellEvent) {
        if (event.board.name == "opponent") {
            event.board.validHint(event.point)
        }
    }


    @Subscribe fun handle(event: MouseExitedCellEvent) {
        if (event.board.name == "opponent") {
            event.board.removeHint(event.point)
        }
    }


    @Subscribe fun handle(event: MouseClickedCellEvent) {
        println("click-${event.board.name}")
    }
}