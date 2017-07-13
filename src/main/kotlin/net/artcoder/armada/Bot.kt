package net.artcoder.armada

interface Bot {

    enum class State {
        SEARCH, TARGET, DESTROY
    }

    enum class DestructionDirection {
        UP, RIGHT, LEFT, DOWN
    }

    enum class CellState {
        NOT_ATTACKED, MISS, HIT
    }

    fun nextPoint(): Point

    fun reportAttack(attackPoint: Point, attackResult: AttackResult)
}