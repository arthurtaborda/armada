package net.artcoder.armada

import net.artcoder.armada.Bot.DestructionDirection.*
import net.artcoder.armada.Bot.State.*

class Bot(private val boardSize: Int, private val pointGenerator: PointGenerator) {

    internal enum class State {
        SEARCHING, TARGET, DESTROYING
    }

    internal enum class DestructionDirection {
        UP, RIGHT, LEFT, DOWN
    }

    internal enum class CellState {
        NOT_ATTACKED, MISS, HIT
    }

    private var state = SEARCHING
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

            if(state == SEARCHING) {
                state = TARGET
                nextPointToAttack = getNextPossibleTargetPoint(attackPoint)
            } else if(state == TARGET) {
                state = DESTROYING
                nextPointToAttack = getNextPossibleDestructionPoint(attackPoint)
            }

        } else if (attackResult == AttackResult.MISS) {
            table[attackPoint.x][attackPoint.y] = CellState.MISS
            if (state == DESTROYING) {
                changeDestructionDirection(attackPoint)
            }
        } else if (attackResult == AttackResult.SUNK) {
            table[attackPoint.x][attackPoint.y] = CellState.HIT
            nextPointToAttack = null
        }
    }

    private fun changeDestructionDirection(attackPoint: Point) {
        when (destructionDirection) {
            UP -> TODO()
            RIGHT -> {
                nextPointToAttack = flipDestroyingDirectionLeft(attackPoint)
            }
            LEFT -> TODO()
            DOWN -> TODO()
            null -> TODO()
        }
    }

    private fun flipDestroyingDirectionLeft(attackPoint: Point): Point {
        destructionDirection = LEFT
        var pointToAttack = attackPoint.left()
        while (hasBeenAttacked(pointToAttack)) {
            pointToAttack = pointToAttack.left()
        }
        return pointToAttack
    }

    private fun getNextPossibleTargetPoint(attackPoint: Point): Point {
        var point = attackPoint.right()
        destructionDirection = RIGHT

        if (hasBeenAttacked(attackPoint.right())) {
            destructionDirection = DOWN
            point = attackPoint.down()
            if (hasBeenAttacked(attackPoint.down())) {
                destructionDirection = LEFT
                point = attackPoint.left()
                if (hasBeenAttacked(attackPoint.left())) {
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
                if (!hasBeenAttacked(attackPoint.up())) {
                    return attackPoint.up()
                } else {
                    return flipDestroyingDirectionDown(attackPoint)
                }
            }
            RIGHT -> {
                if (!hasBeenAttacked(attackPoint.right())) {
                    return attackPoint.right()
                } else {
                    return flipDestroyingDirectionLeft(attackPoint)
                }
            }
            LEFT -> {
                if (!hasBeenAttacked(attackPoint.left())) {
                    return attackPoint.left()
                } else {
                    return flipDestroyingDirectionRight(attackPoint)
                }
            }
            else -> {
                if (!hasBeenAttacked(attackPoint.down())) {
                    return attackPoint.down()
                } else {
                    return flipDestroyingDirectionUp(attackPoint)
                }
            }
        }
    }

    private fun hasBeenAttacked(point: Point): Boolean {
        return table[point.x][point.y] != CellState.NOT_ATTACKED
    }

    private fun flipDestroyingDirectionDown(attackPoint: Point): Point {
        destructionDirection = DOWN
        var pointToAttack = attackPoint.down()
        while (hasBeenAttacked(pointToAttack)) {
            pointToAttack = pointToAttack.down()
        }
        return pointToAttack
    }

    private fun flipDestroyingDirectionRight(attackPoint: Point): Point {
        destructionDirection = RIGHT
        var pointToAttack = attackPoint.right()
        while (hasBeenAttacked(pointToAttack)) {
            pointToAttack = pointToAttack.right()
        }
        return pointToAttack
    }

    private fun flipDestroyingDirectionUp(attackPoint: Point): Point {
        destructionDirection = UP
        var pointToAttack = attackPoint.up()
        while (hasBeenAttacked(pointToAttack)) {
            pointToAttack = pointToAttack.up()
        }
        return pointToAttack
    }

}