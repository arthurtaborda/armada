package net.artcoder.armada.bot

import com.google.common.eventbus.Subscribe
import net.artcoder.armada.bot.SmartBot.CellState.*
import net.artcoder.armada.bot.SmartBot.DestructionDirection.*
import net.artcoder.armada.bot.SmartBot.State.*
import net.artcoder.armada.core.Point
import net.artcoder.armada.match.OpponentHitEvent
import net.artcoder.armada.match.OpponentMissEvent
import net.artcoder.armada.match.OpponentSunkEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

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
    private val hitsNotSunk = LinkedList<Point>()

    private fun cellState(point: Point) = table[point.x][point.y]

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

    @Subscribe fun handle(event: OpponentMissEvent) {
        table[event.pointAttacked.x][event.pointAttacked.y] = MISS
        if (state == DESTROY) {
            log.debug("Bot: DESTROY MISS")
            changeDestructionDirection(event.pointAttacked)
        }
        if (state == TARGET) {
            log.debug("Bot: TARGET MISS")
            nextPointToAttack = getNextPossibleTargetPoint(targetPoint!!)
        }
    }

    @Subscribe fun handle(event: OpponentHitEvent) {
        table[event.pointAttacked.x][event.pointAttacked.y] = HIT
        hitsNotSunk.add(event.pointAttacked)

        when (state) {
            SEARCH  -> {
                log.debug("Bot: SEARCH to TARGET")
                state = TARGET
                targetPoint = event.pointAttacked
                nextPointToAttack = getNextPossibleTargetPoint(event.pointAttacked)
            }
            TARGET  -> {
                log.debug("Bot: TARGET to DESTROY")
                state = DESTROY
                targetPoint = null
                nextPointToAttack = getNextPossibleDestructionPoint(event.pointAttacked)
            }
            DESTROY -> {
                log.debug("Bot: continue to DESTROY")
                nextPointToAttack = getNextPossibleDestructionPoint(event.pointAttacked)
            }
        }
    }

    @Subscribe fun handle(event: OpponentSunkEvent) {
        log.debug("Bot: SUNK")
        table[event.pointAttacked.x][event.pointAttacked.y] = HIT
        hitsNotSunk.removeAll(event.points)
        if(hitsNotSunk.isNotEmpty()) {
            state = DESTROY
            nextPointToAttack = getNextPossibleDestructionPoint(hitsNotSunk.first)
        } else {
            state = SEARCH
            nextPointToAttack = null
        }
    }

    private fun changeDestructionDirection(attackPoint: Point) {
        when (destructionDirection) {
            UP -> nextPointToAttack = flipDestroyingDirectionDown(attackPoint)
            RIGHT -> nextPointToAttack = flipDestroyingDirectionLeft(attackPoint)
            LEFT -> nextPointToAttack = flipDestroyingDirectionRight(attackPoint)
            DOWN -> nextPointToAttack = flipDestroyingDirectionUp(attackPoint)
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
        return !isOutOfBounds(point) && cellState(point) == NOT_ATTACKED
    }

    private fun isOutOfBounds(point: Point): Boolean {
        try {
            cellState(point)
            return false
        } catch(e: ArrayIndexOutOfBoundsException) {
            return true
        }
    }

    private fun flipDestroyingDirection45Degrees() {
        when(destructionDirection) {
            UP    -> destructionDirection = RIGHT
            RIGHT -> destructionDirection = DOWN
            LEFT  -> destructionDirection = UP
            DOWN  -> destructionDirection = LEFT
        }
    }

    private fun flipDestroyingDirectionUp(attackPoint: Point): Point {
        destructionDirection = UP
        var pointToAttack = attackPoint.up()
        while (!isAvailableForAttack(pointToAttack)) {
            pointToAttack = pointToAttack.up()
            if(isOutOfBounds(pointToAttack) || cellState(pointToAttack) == MISS) {
                flipDestroyingDirection45Degrees()
                pointToAttack = getNextPossibleDestructionPoint(hitsNotSunk.first())
            }
        }
        return pointToAttack
    }

    private fun flipDestroyingDirectionRight(attackPoint: Point): Point {
        destructionDirection = RIGHT
        var pointToAttack = attackPoint.right()
        while (!isAvailableForAttack(pointToAttack)) {
            pointToAttack = pointToAttack.right()
            if (isOutOfBounds(pointToAttack) || cellState(pointToAttack) == MISS) {
                flipDestroyingDirection45Degrees()
                pointToAttack = getNextPossibleDestructionPoint(hitsNotSunk.first())
            }
        }
        return pointToAttack
    }

    private fun flipDestroyingDirectionLeft(attackPoint: Point): Point {
        destructionDirection = LEFT
        var pointToAttack = attackPoint.left()
        while (!isAvailableForAttack(pointToAttack)) {
            pointToAttack = pointToAttack.left()
            if (isOutOfBounds(pointToAttack) || cellState(pointToAttack) == MISS) {
                flipDestroyingDirection45Degrees()
                pointToAttack = getNextPossibleDestructionPoint(hitsNotSunk.first())
            }
        }
        return pointToAttack
    }

    private fun flipDestroyingDirectionDown(attackPoint: Point): Point {
        destructionDirection = DOWN
        var pointToAttack = attackPoint.down()
        while (!isAvailableForAttack(pointToAttack)) {
            pointToAttack = pointToAttack.down()
            if (isOutOfBounds(pointToAttack) || cellState(pointToAttack) == MISS) {
                flipDestroyingDirection45Degrees()
                pointToAttack = getNextPossibleDestructionPoint(hitsNotSunk.first())
            }
        }
        return pointToAttack
    }

}