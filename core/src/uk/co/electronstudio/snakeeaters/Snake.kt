package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import uk.co.electronstudio.snakeeaters.Direction.EAST
import uk.co.electronstudio.snakeeaters.Direction.NORTH
import uk.co.electronstudio.snakeeaters.Direction.SOUTH
import uk.co.electronstudio.snakeeaters.Direction.WEST
import uk.co.electronstudio.snakeeaters.SnakeGame.Companion.arena
import uk.me.fantastic.retro.Player

enum class Direction(val vx: Int, val vy: Int) {
    NORTH(0, 1),
    SOUTH(0, -1),
    WEST(-1, 0),
    EAST(1, 0),
}

typealias Point = Pair<Int, Int>
typealias Body = MutableList<Point>

class Snake(
        val player: Player,
        var color: Color,
        var direction: Direction,
        startingPoint: Point,
        var maxLength: Int = 5) {

    val body: Body = ArrayList<Point>()
    var dot: Texture
    val head: Point
        get() = body.first()

    init {
        body.add(startingPoint)
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(color)
        pixmap.drawPixel(0, 0)
        dot = Texture(pixmap)
    }


    fun doDrawing(batch: Batch) {
        for (point in body) {
            batch.draw(dot, point.first.toFloat(), point.second.toFloat())
        }
    }

    fun move() {
        println("test ${wrap(Point(-10,-10))}")

        val oldHead = body.first()
        val newHead = wrap(Point(oldHead.x + direction.vx, oldHead.y + direction.vy))

        println("$newHead")

        // Insert next location at head
        body.add(0, newHead)

        if (body.size > maxLength) {
            body.removeAt(body.lastIndex)
        }
    }

    private fun wrap(point: Point): Point {
        return point.run {
            Point(
                    when {
                        x < arena.left -> arena.right-1
                        x > arena.right-1 -> arena.left
                        else -> x
                    },
                    when {
                        y < arena.bottom -> arena.top-1
                        y > arena.top-1 -> arena.bottom
                        else -> y
                    })
        }
    }

    fun doInput() {
        player.input.leftStick.apply {
            direction = when {
                x < -0.5 -> WEST
                x > 0.5 -> EAST
                y > 0.5 -> SOUTH
                y < -0.5 -> NORTH
                else -> direction
            }
        }
    }

    fun hasCollidedWith(other: Snake) = other.body.any { hasCollidedWith(it) }
    fun hasCollidedWith(point: Point) = (head.x == point.x && head.y == point.y)


}

private val Rectangle.left: Int
    get() = x.toInt()

private val Rectangle.right: Int
    get() = (x + width).toInt()

private val Rectangle.top: Int
    get() = (y+height).toInt()

private val Rectangle.bottom: Int
    get() = y.toInt()




private val <A, B> Pair<A, B>.x: A
    get() = first

private val <A, B> Pair<A, B>.y: B
    get() = second