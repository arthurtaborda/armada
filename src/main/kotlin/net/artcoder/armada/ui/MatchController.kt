package net.artcoder.armada.ui

import net.artcoder.armada.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MatchController(val player: Player, val opponent: Player) : PointClickHandler,
                                                                  PointEnterHandler,
                                                                  PointExitHandler {

    val logger: Logger = LoggerFactory.getLogger(MatchView::class.java)

    val playerBoard = BoardView(doNothingOnClick(),
                                doNothingOnEnter(),
                                doNothingOnExit())

    val opponentBoard = BoardView(this, this, this)
    
    init {
        player.board.placedShips
                .forEach { playerBoard.changeColor(it.points, CellColor.SHIP) }
    }

    private val game = BattleshipGame(player, opponent)

    private val bot = Bot(10, RandomPointGenerator(10))


    fun botAttack(point: Point): AttackResult {
        val botAttack = game.attack(point)
        bot.reportAttack(point, botAttack)
        return botAttack
    }

    override fun enter(point: Point) {
        if (game.canAttack(point)) {
            opponentBoard.validHint(point)
        } else {
            opponentBoard.invalidHint(point)
        }
    }

    override fun exit(point: Point) {
        opponentBoard.removeHint(point)
    }

    override fun click(point: Point) {
        if (game.canAttack(point)) {
            val playerAttackResult = game.attack(point)
            when (playerAttackResult) {
                AttackResult.HIT  -> opponentBoard.changeColor(point, CellColor.HIT)
                AttackResult.MISS -> opponentBoard.changeColor(point, CellColor.MISS)
                AttackResult.SUNK -> {
                    opponent.board
                            .pointsOfShipIn(point)
                            .forEach { opponentBoard.changeColor(it, CellColor.SUNK) }
                }
            }
            val botAttackPoint = bot.nextPoint()
            val botAttackResult = botAttack(botAttackPoint)
            when (botAttackResult) {
                AttackResult.HIT  -> playerBoard.changeColor(botAttackPoint, CellColor.HIT)
                AttackResult.MISS -> playerBoard.changeColor(botAttackPoint, CellColor.MISS)
                AttackResult.SUNK -> {
                    player.board
                            .pointsOfShipIn(botAttackPoint)
                            .forEach { playerBoard.changeColor(it, CellColor.SUNK) }
                }
            }
            logger.debug("Player: ${point} $playerAttackResult")
            logger.debug("Bot: $botAttackPoint $botAttackResult")
        }
    }
}