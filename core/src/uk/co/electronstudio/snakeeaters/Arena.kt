package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import uk.co.electronstudio.retrowar.roundDown

class Arena(game: SnakeGame, levelFile: FileHandle) {
    val levelPixmap = Pixmap(levelFile)
    val level = TextureRegion(Texture(levelPixmap))
    val xOffset = ((game.width - levelPixmap.width) / 2).roundDown()
    val yOffset = 0f

    fun getRandomPoint() = Point(MathUtils.random(levelPixmap.width - 1), MathUtils.random(levelPixmap.height - 1))

    fun getRandomEmptyPoint(): Point {
        while (true) {
            val point = getRandomPoint()
            if (emptyCellAt(point)
                && emptyCellAt(point.x, point.y + 1) // check adjacent points are empty too
                && emptyCellAt(point.x, point.y - 1)
                && emptyCellAt(point.x + 1, point.y)
                && emptyCellAt(point.x - 1, point.y)
            ) {
                return point
            }
        }
    }

    fun emptyCellAt(x: Int, y: Int): Boolean {
        val c = Color(levelPixmap.getPixel(x, levelPixmap.height - y - 1))
        return c.a == 0f
    }

    fun emptyCellAt(point: Point): Boolean {
        return emptyCellAt(point.x, point.y)

    }

    fun doDrawing(batch: Batch) {
        batch.draw(level, xOffset, yOffset)
    }
}
