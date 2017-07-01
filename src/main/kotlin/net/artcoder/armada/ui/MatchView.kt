package net.artcoder.armada.ui

import net.artcoder.armada.AttackResult
import net.artcoder.armada.Player
import tornadofx.*

class MatchView(playerOne: Player, playerTwo: Player) : View() {

    constructor() : this(RandomPlayer.create(), RandomPlayer.create())

    private val controller = MatchController(playerOne, playerTwo)

    private val playerBoard = BoardView(playerOne.board)

    private val opponentBoard = BoardView()

    override val root = hbox {
        add(playerBoard)
        add(opponentBoard)
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
            if (event.board == opponentBoard) {
                val playerAttackResult = controller.attack(event.point)
                when (playerAttackResult) {
                    AttackResult.HIT  -> opponentBoard.changeColor(event.point, CellColor.HIT)
                    AttackResult.MISS -> opponentBoard.changeColor(event.point, CellColor.MISS)
                    AttackResult.SUNK -> opponentBoard.changeColor(event.point, CellColor.SUNK)
                }
                val botAttackPoint = controller.nextBotAttack()
                val botAttackResult = controller.botAttack(botAttackPoint)
                when (botAttackResult) {
                    AttackResult.HIT  -> playerBoard.changeColor(botAttackPoint, CellColor.HIT)
                    AttackResult.MISS -> playerBoard.changeColor(botAttackPoint, CellColor.MISS)
                    AttackResult.SUNK -> playerBoard.changeColor(botAttackPoint, CellColor.SUNK)
                }
                println("Player: ${event.point} $playerAttackResult")
                println("Bot: $botAttackPoint $botAttackResult")
            }
        }
    }
}