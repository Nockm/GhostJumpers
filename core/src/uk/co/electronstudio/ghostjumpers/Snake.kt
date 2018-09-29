package uk.co.electronstudio.ghostjumpers

import uk.me.fantastic.retro.Player

enum class Direction(val vx: Int, val vy: Int) {
    NORTH(0, 1),
    SOUTH(-1,0),
    WEST(-1, 0),
    EAST(1, 0),
}

typealias Point = Pair<Int,Int>
typealias Body = MutableList<Point>

class Snake(val player: Player, var direction: Direction, startingPoint: Point) {
    val body: Body = ArrayList<Point>()
    init {
        body.add(startingPoint)
    }


}