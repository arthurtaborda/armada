package net.artcoder.armada

import net.artcoder.armada.Bot.DestructionDirection.*
import net.artcoder.armada.Bot.State.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Bot(private val boardSize: Int, private val pointGenerator: PointGenerator) {

    val log: Logger = LoggerFactory.getLogger(Bot::class.java)

    internal enum class State {
        SEARCH, TARGET, DESTROY
    }

    internal enum class DestructionDirection {
        UP, RIGHT, LEFT, DOWN
    }

    internal enum class CellState {
        NOT_ATTACKED, MISS, HIT
    }

    private var state = SEARCH
    private var destructionDirection: DestructionDirection? = null
    private var nextPointToAttack: Point? = null
    private val table = Array(boardSize) { Array(boardSize) { CellState.NOT_ATTACKED } } // matrix of (size x size) with all false elements

    fun nextPoint(): Point {
        if (nextPointToAttack != null) {
            return nextPointToAttack!!
        }

        return pointGenerator.randomPoint()
    }

    fun reportAttack(attackPoint: Point, attackResult: AttackResult) {
        if (attackResult == AttackResult.HIT) {
            table[attackPoint.x][attackPoint.y] = CellState.HIT

            if(state == SEARCH) {
                log.debug("Bot: SEARCH to TARGET")
                state = TARGET
                nextPointToAttack = getNextPossibleTargetPoint(attackPoint)
            } else if(state == TARGET) {
                log.debug("Bot: TARGET to DESTROY")
                state = DESTROY
                nextPointToAttack = getNextPossibleDestructionPoint(attackPoint)
            }

        } else if (attackResult == AttackResult.MISS) {
            table[attackPoint.x][attackPoint.y] = CellState.MISS
            if (state == DESTROY) {
                log.debug("Bot: DESTROY MISS")
                changeDestructionDirection(attackPoint)
            }
        } else if (attackResult == AttackResult.SUNK) {
            log.debug("Bot: SUNK")
            table[attackPoint.x][attackPoint.y] = CellState.HIT
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
            return table[point.x][point.y] == CellState.NOT_ATTACKED
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