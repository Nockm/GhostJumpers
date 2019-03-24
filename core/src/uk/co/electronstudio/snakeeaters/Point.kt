package uk.co.electronstudio.snakeeaters

class Point(val x: Int, val y: Int) {
    operator fun plus(direction: Direction): Point {
        return Point(x + direction.vx, y + direction.vy)
    }

    operator fun minus(direction: Direction): Point {
        return Point(x - direction.vx, y - direction.vy)
    }

    fun wrap(arena: Arena): Point {
        return Point(when {
            x < 0 -> arena.levelPixmap.width - 1
            x > arena.levelPixmap.width - 1 -> 0
            else -> x
        }, when {
            y < 0 -> arena.levelPixmap.height - 1
            y > arena.levelPixmap.height - 1 -> 0
            else -> y
        })
    }
}