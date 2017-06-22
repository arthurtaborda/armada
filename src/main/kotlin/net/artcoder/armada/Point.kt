package net.artcoder.armada

interface Point {

    val x: Int
    val y: Int

    fun up(): Point
    fun right(): Point
    fun left(): Point
    fun down(): Point
}