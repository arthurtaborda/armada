package net.artcoder.armada.ships

import net.artcoder.armada.Ship

abstract class AbstractShip : Ship {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ship) return false

        if (name != other.name) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + size
        return result
    }
}