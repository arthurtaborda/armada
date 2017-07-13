package net.artcoder.armada.bot

import net.artcoder.armada.core.Point

interface Bot {

    fun nextPoint(): Point
}