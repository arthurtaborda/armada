package net.artcoder.armada.ui

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import net.artcoder.armada.SetupBoard


class SetupBoardController(private val eventBus: EventBus) {

    private val setupBoard = SetupBoard(2, 2, 3, 3, 4, 5)

    @Subscribe fun handle(event: MouseEnteredCellEvent) {
        val placingPoints = setupBoard.nextPlacingPoints(event.point)
        if (placingPoints.validPlacement) {
            event.board.validHint(placingPoints.points)
        } else {
            event.board.invalidHint(placingPoints.points)
        }
    }


    @Subscribe fun handle(event: MouseExitedCellEvent) {
        event.board.removeHint(setupBoard.nextPlacingPoints(event.point).points)
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
        eventBus.post(GameStartedEvent())
    }
}

class CanStartGameEvent
class GameStartedEvent