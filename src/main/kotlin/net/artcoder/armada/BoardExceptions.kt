package net.artcoder.armada

class PointAlreadyAttackedException(val point: Point) : RuntimeException("Point $point is already attacked")