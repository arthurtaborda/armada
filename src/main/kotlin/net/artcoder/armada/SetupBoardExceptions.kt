package net.artcoder.armada

class ShipsUnplacedException(val ships: List<Ship>) : RuntimeException("There are ships to be placed")