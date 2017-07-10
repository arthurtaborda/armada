package net.artcoder.armada.ui

import net.artcoder.armada.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MatchController(private val player: Player,
                      private val opponent: Player,
                      private val gameResultAnnouncer: GameResultAnnouncer) : PointClickHandler,
                                                                              PointEnterHandler,
                                                                              PointExitHandler {

    private val logger: Logger = LoggerFactory.getLogger(MatchView::class.java)

    val playerBoard = BoardView(doNothingOnClick(),
                                doNothingOnEnter(),
                                doNothingOnExit())

    val opponentBoard = BoardView(this, this, this)

    init {
        player.pointsOfPlacedShips()
                .forEach { playerBoard.changeColor(it, CellColor.SHIP) }
    }

    private val game = BattleshipGame(player, opponent)

    private val bot = Bot(10, RandomPointGenerator(10))


    private fun botAttack() {
        val botAttackPoint = bot.nextPoint()
        val botAttackResult = game.attack(botAttackPoint)
        logger.debug("Bot: $botAttackPoint $botAttackResult")

        bot.reportAttack(botAttackPoint, botAttackResult)
        when (botAttackResult) {
            AttackResult.HIT  -> {
                playerBoard.changeColor(botAttackPoint, CellColor.HIT)
                botAttack()
            }
            AttackResult.MISS -> playerBoard.changeColor(botAttackPoint, CellColor.MISS)
            AttackResult.SUNK -> {
                player.board
                        .pointsOfShipIn(botAttackPoint)
                        .forEach { playerBoard.changeColor(it, CellColor.SUNK) }

                if (opponent.state == Player.State.WON) {
                    gameResultAnnouncer.announceLoser()
                } else {
                    botAttack()
                }
            }
        }

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
                AttackResult.HIT  -> {
                    opponentBoard.changeColor(point, CellColor.HIT)
                }
                AttackResult.MISS -> {
                    opponentBoard.changeColor(point, CellColor.MISS)
                }
                AttackResult.SUNK -> {
                    opponent.board
                            .pointsOfShipIn(point)
                            .forEach { opponentBoard.changeColor(it, CellColor.SUNK) }

                    if (player.state == Player.State.WON) {
                        gameResultAnnouncer.announceWinner()
                    }
                }
            }

            if (game.attackingPlayer() == opponent) {
                botAttack()
            }

            logger.debug("Player: ${point} $playerAttackResult")
        }
    }
}