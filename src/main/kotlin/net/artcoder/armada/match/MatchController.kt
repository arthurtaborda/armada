package net.artcoder.armada.match

import com.google.common.eventbus.Subscribe
import net.artcoder.armada.core.gui.MouseClickedCellEvent
import net.artcoder.armada.core.gui.MouseEnteredCellEvent
import net.artcoder.armada.core.gui.MouseExitedCellEvent

class MatchController(private val match: SinglePlayMatch,
                      private val opponentBoardName: String) {

    private var playerTurn = true

    @Subscribe fun handle(event: MouseEnteredCellEvent) {
        if (playerTurn && event.board.name == opponentBoardName) {
            if (match.canAttack(event.point)) {
                event.board.validHint(event.point)
            } else {
                event.board.invalidHint(event.point)
            }
        }
    }

    @Subscribe fun handle(event: PlayerMissEvent) {
        playerTurn = false
    }

    @Subscribe fun handle(event: OpponentMissEvent) {
        playerTurn = true
    }


    @Subscribe fun handle(event: MouseExitedCellEvent) {
        if (playerTurn && event.board.name == opponentBoardName) {
            event.board.removeHint(event.point)
        }
    }

    @Subscribe fun handle(event: MouseClickedCellEvent) {
        if (playerTurn && event.board.name == opponentBoardName) {
            if (match.canAttack(event.point)) {
                match.attack(event.point)
            }
        }
    }
}