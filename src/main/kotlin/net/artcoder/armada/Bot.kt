package net.artcoder.armada

interface Bot {

    fun nextPoint(): Point

    fun reportAttack(attackPoint: Point, attackResult: AttackResult)
}