package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Colors
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import uk.co.electronstudio.retrowar.Color
import uk.co.electronstudio.snakeeaters.Direction.EAST
import uk.co.electronstudio.snakeeaters.Direction.NORTH
import uk.co.electronstudio.snakeeaters.Direction.SOUTH
import uk.co.electronstudio.snakeeaters.Direction.WEST
import uk.co.electronstudio.retrowar.Player


enum class Direction(val vx: Int, val vy: Int) {
    NORTH(0, 1), SOUTH(0, -1), WEST(-1, 0), EAST(1, 0),
}

typealias Body = MutableList<Point>

fun findGoodColor(vararg colors: Color): Color {
    val c = colors.firstOrNull{it !=  Color(0, 0, 0) && it != Color(255,255,255)}
    return c ?: Color.PURPLE
}

class Snake(val game: SnakeGame, val player: Player, var direction: Direction, startingPoint: Point,
            var maxLength: Int = 10) {
    private var requestedDirection: Direction = direction
    private val body: Body = arrayListOf(startingPoint)
    private val dot1: Texture =  makePixel(findGoodColor(player.color2, player.color))



    //  private val dot2: Texture = makePixel(player.color)
    private val dot3: Texture = makePixel(Color.WHITE)
    var head: Point = startingPoint
    private var flash = false

    fun doDrawing(batch: Batch) {
        for (point in body) {
            batch.draw(if (flash) dot3 else dot1, point.x.toFloat()+game.arena.xOffset, point.y.toFloat()+game.arena.yOffset)
        }
        batch.draw(dot1,head.x.toFloat()+game.arena.xOffset,head.y.toFloat()+game.arena.yOffset)
        flash = false
    }

    fun move() {
        direction = when (requestedDirection){
            WEST -> if (direction == EAST) EAST else WEST
            EAST -> if (direction == WEST) WEST else EAST
            SOUTH-> if (direction == NORTH) NORTH else SOUTH
            NORTH -> if (direction == SOUTH) SOUTH else NORTH
        }
        body.add(0, head)
        head += direction
        head = head.wrap(game.arena)
        while (body.size > maxLength) {
            body.removeAt(body.lastIndex)
        }
    }

    fun doInput() {
        player.input.leftStick.apply {
            requestedDirection = when {
                x < -0.6 ->  WEST
                x > 0.6 -> EAST
                y > 0.6 ->SOUTH
                y < -0.6 ->  NORTH
                else -> requestedDirection
            }
        }
    }

    fun hasCollidedWith(other: Snake) = other.body.any { hasCollidedWith(it) }

    fun hasCollidedWith(point: Point) = (head.x == point.x && head.y == point.y)

    fun doCollision() {
        game.stunSound.play()
        body.removeAt(0)
        head -= direction
        maxLength -= 2
        flash = true
    }
}

