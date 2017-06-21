package net.artcoder.armada

class PlacementOutOfBoundsException(point: Point): RuntimeException("Placement is out of bounds in (${point.x},${point.y})")

class ShipOverlapException(point: Point) : RuntimeException("A ship exists in (${point.x},${point.y})")

class ShipsUnplacedException(val ships: List<Ship>) : RuntimeException("There are ships to be placed")

class ShipNotAvailableException(ship: Ship) : RuntimeException("Ship ${ship.name} is not available")