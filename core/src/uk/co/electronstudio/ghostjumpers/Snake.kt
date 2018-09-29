package uk.co.electronstudio.ghostjumpers

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import uk.me.fantastic.retro.Player

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
        if (player.input.leftStick.x < -0.5) { direction = Direction.WEST } // ok
        else if (player.input.leftStick.x > 0.5) { direction = Direction.EAST}
        else if (player.input.leftStick.y < -0.5) { direction = Direction.NORTH }
        else if (player.input.leftStick.y > 0.5) { direction = Direction.SOUTH }
    }

    fun hasCollidedWith(snake2: Snake): Boolean {
        val myHead = body.first()

        snake2.body.forEach{
            if(myHead.x == it.x && myHead.y == it.y) {
                println("Dead stuff")
                return true
            }
        }
        return false
    }

    val body: Body = ArrayList<Point>()
    var dot: Texture
    init {
        body.add(startingPoint)
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(color)
        pixmap.drawPixel(0,0)
        dot = Texture(pixmap)
    }
}

private val <A, B> Pair<A, B>.x: A
    get() = first

private val <A, B> Pair<A, B>.y: B
    get() = second