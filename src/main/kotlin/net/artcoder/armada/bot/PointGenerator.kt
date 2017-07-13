package net.artcoder.armada.bot

import net.artcoder.armada.core.Point

interface PointGenerator {

    fun randomPoint(): Point
}