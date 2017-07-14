package net.artcoder.armada.ai

import net.artcoder.armada.core.Point

interface Bot {

    fun nextPoint(): Point
}