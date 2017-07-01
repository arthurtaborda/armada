package net.artcoder.armada.ui

import net.artcoder.armada.AttackResult
import net.artcoder.armada.Player
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tornadofx.*

class MatchView(playerOne: Player, playerTwo: Player) : View() {

    val logger: Logger = LoggerFactory.getLogger(MatchView::class.java)

    constructor() : this(RandomPlayer.create(), RandomPlayer.create())

    private val controller = MatchController(playerOne, playerTwo)

    private val playerBoard = BoardView(playerOne.board)

    private val opponentBoard = BoardView()

    override val root = hbox {
        vbox {
            label("You")
            add(playerBoard)
        }
        vbox {
            label("Opponent")
            add(opponentBoard)
        }
    }

    init {
        subscribe<MouseEnteredCellEvent> { event ->
            if (event.board == opponentBoard) {
                if (controller.canAttack(event.point)) {
                    opponentBoard.validHint(event.point)
                } else {
                    opponentBoard.invalidHint(event.point)
                }
            }
        }
        subscribe<MouseExitedCellEvent> { event ->
            if (event.board == opponentBoard) {
                opponentBoard.removeHint(event.point)
            }
        }
        subscribe<MouseClickedCellEvent> { event ->
            if (event.board == opponentBoard && controller.canAttack(event.point)) {
                val playerAttackResult = controller.attack(event.point)
                when (playerAttackResult) {
                    AttackResult.HIT  -> opponentBoard.changeColor(event.point, CellColor.HIT)
                    AttackResult.MISS -> opponentBoard.changeColor(event.point, CellColor.MISS)
                    AttackResult.SUNK -> {
                        playerTwo.board
                                .pointsOfShipIn(event.point)
                                .forEach { opponentBoard.changeColor(it, CellColor.SUNK) }
                    }
                }
                val botAttackPoint = controller.nextBotAttack()
                val botAttackResult = controller.botAttack(botAttackPoint)
                when (botAttackResult) {
                    AttackResult.HIT  -> playerBoard.changeColor(botAttackPoint, CellColor.HIT)
                    AttackResult.MISS -> playerBoard.changeColor(botAttackPoint, CellColor.MISS)
                    AttackResult.SUNK -> {
                        playerOne.board
                                .pointsOfShipIn(botAttackPoint)
                                .forEach { playerBoard.changeColor(it, CellColor.SUNK) }
                    }
                }
                logger.debug("Player: ${event.point} $playerAttackResult")
                logger.debug("Bot: $botAttackPoint $botAttackResult")
            }
        }
    }
}