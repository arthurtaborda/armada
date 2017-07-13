package net.artcoder.armada

import net.artcoder.armada.SmartBot.CellState.*
import net.artcoder.armada.SmartBot.DestructionDirection.*
import net.artcoder.armada.SmartBot.State.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class SmartBot(private val pointGenerator: PointGenerator): Bot {

    val log: Logger = LoggerFactory.getLogger(SmartBot::class.java)

    enum class State {
        SEARCH, TARGET, DESTROY
    }

    enum class DestructionDirection {
        UP, RIGHT, LEFT, DOWN
    }

    enum class CellState {
        NOT_ATTACKED, MISS, HIT
    }

    private val boardSize = 10

    private var state = SEARCH
    private var destructionDirection: DestructionDirection? = null
    private var targetPoint: Point? = null
    private var nextPointToAttack: Point? = null
    private val table = Array(boardSize) { Array(boardSize) { NOT_ATTACKED } } // matrix of (size x size) with all false elements

    override fun nextPoint(): Point {
        if (nextPointToAttack != null) {
            return nextPointToAttack!!
        }

        while (true) {
            val randomPoint = pointGenerator.randomPoint()
            if(isAvailableForAttack(randomPoint)) {
                return randomPoint
            }
        }
    }

    override fun reportAttack(attackPoint: Point, attackResult: AttackResult) {
        if (attackResult == AttackResult.HIT) {
            table[attackPoint.x][attackPoint.y] = HIT

            when (state) {
                SEARCH  -> {
                    log.debug("Bot: SEARCH to TARGET")
                    state = TARGET
                    targetPoint = attackPoint
                    nextPointToAttack = getNextPossibleTargetPoint(attackPoint)
                }
                TARGET  -> {
                    log.debug("Bot: TARGET to DESTROY")
                    state = DESTROY
                    targetPoint = null
                    nextPointToAttack = getNextPossibleDestructionPoint(attackPoint)
                }
                DESTROY -> {
                    log.debug("Bot: continue to DESTROY")
                    nextPointToAttack = getNextPossibleDestructionPoint(attackPoint)
                }
            }
        } else if (attackResult == AttackResult.MISS) {
            table[attackPoint.x][attackPoint.y] = MISS
            if (state == DESTROY) {
                log.debug("Bot: DESTROY MISS")
                changeDestructionDirection(attackPoint)
            }
            if (state == TARGET) {
                log.debug("Bot: TARGET MISS")
                nextPointToAttack = getNextPossibleTargetPoint(targetPoint!!)
            }
        } else if (attackResult == AttackResult.SUNK) {
            state = SEARCH
            log.debug("Bot: SUNK")
            table[attackPoint.x][attackPoint.y] = HIT
            nextPointToAttack = null
        }
    }

    private fun changeDestructionDirection(attackPoint: Point) {
        when (destructionDirection) {
            UP -> nextPointToAttack = flipDestroyingDirectionDown(attackPoint)
            RIGHT -> nextPointToAttack = flipDestroyingDirectionLeft(attackPoint)
            LEFT -> nextPointToAttack = flipDestroyingDirectionRight(attackPoint)
            else -> nextPointToAttack = flipDestroyingDirectionUp(attackPoint)
        }
    }

    private fun getNextPossibleTargetPoint(attackPoint: Point): Point {
        var point = attackPoint.right()
        destructionDirection = RIGHT

        if (!isAvailableForAttack(attackPoint.right())) {
            destructionDirection = DOWN
            point = attackPoint.down()
            if (!isAvailableForAttack(attackPoint.down())) {
                destructionDirection = LEFT
                point = attackPoint.left()
                if (!isAvailableForAttack(attackPoint.left())) {
                    destructionDirection = UP
                    point = attackPoint.up()
                }
            }
        }

        return point
    }

    private fun getNextPossibleDestructionPoint(attackPoint: Point): Point {
        when (destructionDirection) {
            UP -> {
                if (isAvailableForAttack(attackPoint.up())) {
                    return attackPoint.up()
                } else {
                    return flipDestroyingDirectionDown(attackPoint)
                }
            }
            RIGHT -> {
                if (isAvailableForAttack(attackPoint.right())) {
                    return attackPoint.right()
                } else {
                    return flipDestroyingDirectionLeft(attackPoint)
                }
            }
            LEFT -> {
                if (isAvailableForAttack(attackPoint.left())) {
                    return attackPoint.left()
                } else {
                    return flipDestroyingDirectionRight(attackPoint)
                }
            }
            else -> {
                if (isAvailableForAttack(attackPoint.down())) {
                    return attackPoint.down()
                } else {
                    return flipDestroyingDirectionUp(attackPoint)
                }
            }
        }
    }

    private fun isAvailableForAttack(point: Point): Boolean {
        try {
            return table[point.x][point.y] == NOT_ATTACKED
        } catch(e: ArrayIndexOutOfBoundsException) {
            return false
        }
    }

    private fun flipDestroyingDirectionUp(attackPoint: Point): Point {
        destructionDirection = UP
        var pointToAttack = attackPoint.up()
        while (!isAvailableForAttack(pointToAttack)) {
            pointToAttack = pointToAttack.up()
        }
        return pointToAttack
    }

    private fun flipDestroyingDirectionRight(attackPoint: Point): Point {
        destructionDirection = RIGHT
        var pointToAttack = attackPoint.right()
        while (!isAvailableForAttack(pointToAttack)) {
            pointToAttack = pointToAttack.right()
        }
        return pointToAttack
    }

    private fun flipDestroyingDirectionLeft(attackPoint: Point): Point {
        destructionDirection = LEFT
        var pointToAttack = attackPoint.left()
        while (!isAvailableForAttack(pointToAttack)) {
            pointToAttack = pointToAttack.left()
        }
        return pointToAttack
    }

    private fun flipDestroyingDirectionDown(attackPoint: Point): Point {
        destructionDirection = DOWN
        var pointToAttack = attackPoint.down()
        while (!isAvailableForAttack(pointToAttack)) {
            pointToAttack = pointToAttack.down()
        }
        return pointToAttack
    }

}