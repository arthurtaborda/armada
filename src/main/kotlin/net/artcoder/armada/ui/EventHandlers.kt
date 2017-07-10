package net.artcoder.armada.ui

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import net.artcoder.armada.Point

fun doNothingOnClick(): PointClickHandler = object : PointClickHandler {
    override fun click(point: Point) {
    }
}

fun doNothingOnEnter(): PointEnterHandler = object : PointEnterHandler {
    override fun enter(point: Point) {
    }
}

fun doNothingOnExit(): PointExitHandler = object : PointExitHandler {
    override fun exit(point: Point) {
    }
}

interface PointClickHandler {

    fun click(point: Point)
}

interface PointEnterHandler {

    fun enter(point: Point)
}

interface PointExitHandler {

    fun exit(point: Point)
}