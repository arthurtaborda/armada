package net.artcoder.armada.bot

import net.artcoder.armada.core.Point
import net.artcoder.armada.match.AttackResult

interface Bot {

    fun nextPoint(): Point

    fun reportAttack(attackPoint: Point, attackResult: AttackResult)
}