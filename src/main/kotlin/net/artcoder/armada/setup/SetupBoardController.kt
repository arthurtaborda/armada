package net.artcoder.armada.setup

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import net.artcoder.armada.match.Board
import net.artcoder.armada.core.gui.MouseClickedCellEvent
import net.artcoder.armada.core.gui.MouseEnteredCellEvent
import net.artcoder.armada.core.gui.MouseExitedCellEvent


class SetupBoardController(private val eventBus: EventBus,
                           private val setupBoard: SetupBoard) {

    @Subscribe fun handle(event: MouseEnteredCellEvent) {
        val placingPoints = setupBoard.placingPointsFrom(event.point)
        if (placingPoints.validPlacement) {
            event.board.validHint(placingPoints.points)
        } else {
            event.board.invalidHint(placingPoints.points)
        }
    }


    @Subscribe fun handle(event: MouseExitedCellEvent) {
        event.board.removeHint(setupBoard.placingPointsFrom(event.point).points)
    }


    @Subscribe fun handle(event: MouseClickedCellEvent) {
        val pointsPlaced = setupBoard.place(event.point)
        event.board.ship(pointsPlaced)
        if(setupBoard.allShipsPlaced) {
            eventBus.post(CanStartGameEvent())
        }
    }

    fun rotate() {
        setupBoard.rotate()
    }

    fun start() {
        eventBus.post(GameStartedEvent(setupBoard.finish()))
    }
}

class CanStartGameEvent
data class GameStartedEvent(val board: Board)