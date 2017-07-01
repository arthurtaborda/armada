package net.artcoder.armada.ui

import net.artcoder.armada.*
import tornadofx.*

class MatchController(playerOne: Player, playerTwo: Player): Controller() {

    val game = BattleshipGame(playerOne, playerTwo)
    val bot = Bot(10, RandomPointGenerator(10))

    fun canAttack(point: Point): Boolean {
        return game.canAttack(point)
    }

    fun attack(point: Point): AttackResult {
        return game.attack(point)
    }

    fun botAttack(point: Point): AttackResult {
        val botAttack = game.attack(point)
        bot.reportAttack(point, botAttack)
        return botAttack
    }

    fun nextBotAttack(): Point {
        return bot.nextPoint()
    }
}