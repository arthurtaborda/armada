package net.artcoder.armada.ui

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import net.artcoder.armada.BattleshipMatch

class MatchController(private val eventBus: EventBus,
                      private val match: BattleshipMatch) {

    @Subscribe fun handle(event: MouseEnteredCellEvent) {
        if (event.board.name == "opponent") {
            if (match.canAttack(event.point)) {
                event.board.validHint(event.point)
            } else {
                event.board.invalidHint(event.point)
            }
        }
    }


    @Subscribe fun handle(event: MouseExitedCellEvent) {
        if (event.board.name == "opponent") {
            event.board.removeHint(event.point)
        }
    }

    @Subscribe fun handle(event: MouseClickedCellEvent) {
        if (event.board.name == "opponent") {
            if (match.canAttack(event.point)) {
                match.attack(event.point)
            }
        }
    }
}