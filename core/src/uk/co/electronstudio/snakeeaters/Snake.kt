package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import uk.me.fantastic.retro.Player
import uk.co.electronstudio.snakeeaters.Direction.*

enum class Direction(val vx: Int, val vy: Int) {
    NORTH(0, 1),
    SOUTH(0,-1),
    WEST(-1, 0),
    EAST(1, 0),
}

typealias Point = Pair<Int,Int>
typealias Body = MutableList<Point>

class Snake(
        val player: Player,
        var color: Color,
        var direction: Direction,
        startingPoint: Point,
        var maxLength: Int = 5)
{

    val body: Body = ArrayList<Point>()
    var dot: Texture
    val head: Point
        get() = body.first()

    init {
        body.add(startingPoint)
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(color)
        pixmap.drawPixel(0,0)
        dot = Texture(pixmap)
    }


    fun doDrawing(batch: Batch) {
        for (point in body) {
            batch.draw(dot, point.first.toFloat(), point.second.toFloat())
        }
    }

    fun move() {
        val oldHead = body.first()
        val newHead = Point(oldHead.x + direction.vx, oldHead.y + direction.vy)

        // Insert next location at head
        body.add(0, newHead)

        if (body.size > maxLength) {
            body.removeAt(body.lastIndex)
        }
    }

    fun doInput() {
        player.input.leftStick.apply {
            direction = when{
                x < -0.5 -> WEST
                x > 0.5 -> EAST
                y > 0.5 -> SOUTH
                y < -0.5 -> NORTH
                else -> direction
            }
        }
    }

    fun hasCollidedWith(other: Snake): Boolean {
        other.body.forEach{
            if(head.x == it.x && head.y == it.y) {
                return true
            }
        }
        return false
    }



}

private val <A, B> Pair<A, B>.x: A
    get() = first

private val <A, B> Pair<A, B>.y: B
    get() = second