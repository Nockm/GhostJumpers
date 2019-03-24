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
import uk.co.electronstudio.retrowar.Player


enum class Direction(val vx: Int, val vy: Int) {
    NORTH(0, 1), SOUTH(0, -1), WEST(-1, 0), EAST(1, 0),
}


typealias Body = MutableList<Point>

class Snake(val game: SnakeGame, val player: Player, var direction: Direction, startingPoint: Point,
            var maxLength: Int = 10) {

    val body: Body = ArrayList<Point>()
    var dot1: Texture
    val dot2: Texture
    val dot3: Texture
    var head: Point = startingPoint
    var flash = false

    init {
        body.add(startingPoint)
        dot1 = makePixel(player.color2)
        dot2 = makePixel(player.color)
        dot3 = makePixel(Color.WHITE)
    }


    fun doDrawing(batch: Batch) {
        for (point in body) {
            batch.draw(if (flash) dot3 else dot1, point.x.toFloat()+game.arena.xOffset, point.y.toFloat()+game.arena.yOffset)
        }
        batch.draw(dot1, head.x.toFloat()+game.arena.xOffset, head.y.toFloat()+game.arena.yOffset)
        flash = false
    }

    fun move() {
        body.add(0, head)
        head += direction
        head = head.wrap(game.arena)

        while (body.size > maxLength) {
            body.removeAt(body.lastIndex)
        }
    }


    fun doInput() {
        player.input.leftStick.apply {
            direction = when {
                x < -0.3 -> if (direction == EAST) EAST else WEST
                x > 0.3 -> if (direction == WEST) WEST else EAST
                y > 0.3 -> if (direction == NORTH) NORTH else SOUTH
                y < -0.3 -> if (direction == SOUTH) SOUTH else NORTH
                else -> direction
            }
        }
    }

    fun hasCollidedWith(other: Snake) = other.body.any { hasCollidedWith(it) }


    fun hasCollidedWith(point: Point) = (head.x == point.x && head.y == point.y)
    fun doCollision() {
        if(game.timer<4f) return // invulnerable at start
        game.stunSound.play()
        body.removeAt(0)
        head -= direction
        maxLength -= 2
        flash = true
    }


}


private val Rectangle.left: Int
    get() = x.toInt()

private val Rectangle.right: Int
    get() = (x + width).toInt()

private val Rectangle.top: Int
    get() = (y + height).toInt()

private val Rectangle.bottom: Int
    get() = y.toInt()


