package net.artcoder.armada.ui

import com.google.common.eventbus.Subscribe
import net.artcoder.armada.OpponentMissEvent
import net.artcoder.armada.PlayerMissEvent
import net.artcoder.armada.SinglePlayMatch

class MatchController(private val match: SinglePlayMatch) {

    private var playerTurn = true

    @Subscribe fun handle(event: MouseEnteredCellEvent) {
        if (playerTurn && event.board.name == "opponent") {
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
        if (playerTurn && event.board.name == "opponent") {
            event.board.removeHint(event.point)
        }
    }

    @Subscribe fun handle(event: MouseClickedCellEvent) {
        if (playerTurn && event.board.name == "opponent") {
            if (match.canAttack(event.point)) {
                match.attack(event.point)
            }
        }
    }
}