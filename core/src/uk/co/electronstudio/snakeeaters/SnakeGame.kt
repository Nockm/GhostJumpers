package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import uk.me.fantastic.retro.App
import uk.me.fantastic.retro.Player
import uk.me.fantastic.retro.Prefs
import uk.me.fantastic.retro.SimpleGame
import uk.me.fantastic.retro.screens.GameSession

fun makePixel(color: Color): Texture {
    val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
    pixmap.setColor(color)
    pixmap.drawPixel(0, 0)
    return Texture(pixmap)
}

var arena = Rectangle(19f,0f,50f,50f)

/* The God class */
class SnakeGame(session: GameSession, pathPrefix: String = "") :
        SimpleGame(session,
                88f, 50f, BitmapFont(Gdx.files.internal(pathPrefix+"4pix.fnt")), false) {





    val jumpSound = Gdx.audio.newSound(Gdx.files.internal(pathPrefix+"jump_jade.wav"))
    val stunSound = Gdx.audio.newSound(Gdx.files.internal(pathPrefix+"fall_jade.wav"))
    val bonusSound = Gdx.audio.newSound(Gdx.files.internal(pathPrefix+"bonus_jade.wav"))
    val spawnSound = Gdx.audio.newSound(Gdx.files.internal(pathPrefix+"hit_jade.wav"))
    val music = CrossPlatformMusic.create(desktopFile = pathPrefix+"justin1.ogg", androidFile =
    pathPrefix+"JustinLong.ogg", iOSFile = pathPrefix+"justin1.wav")

    val multiFlash = Animation<Texture>(1f / 30f,
            makePixel(Color.RED),
            makePixel(Color.BLUE),
            makePixel(Color.GREEN)
    ).also {
        it.playMode = Animation.PlayMode.LOOP
    }

    var timer = 0f
    var delay = 0.1f

    val snakes = ArrayList<Snake>()
    var food : Point

    init {
        food = getRandomPoint()
    }

    private fun getRandomPoint() = Point(
            MathUtils.random(arena.x.toInt()+10, arena.x.toInt()+arena.width.toInt()-10),
            MathUtils.random(arena.y.toInt()+10, arena.y.toInt()+arena.height.toInt()-10)
    )


    fun tick() {
        for (snake in snakes) {
            snake.move()
        }

        val deadSnakes = ArrayList<Snake>()

        for (snake1 in snakes) {
            if (snake1.hasCollidedWith(food)){
                food = getRandomPoint()
                snake1.maxLength++
            }
            for (snake2 in snakes) {
                if (snake1 != snake2) {
                    if (snake1.hasCollidedWith(snake2)) {
                        deadSnakes.add(snake1)
                    }
                }
            }
        }

        snakes.removeAll(deadSnakes)
        deadSnakes.clear()
    }

    override fun playerJoined(player: Player) {
        snakes.add(Snake(player, player.color2, Direction.SOUTH, getRandomPoint()))
    }

    override fun doLogic(deltaTime: Float) {
        timer += deltaTime

        if (timer > delay) {
            timer = 0f
            tick()
        }

        for (snake in snakes) {
            snake.doInput()
        }
    }


    private fun doGameoverLogic() {
//        if (timer > 1f && players.any { it.input.fire }) {
//        }
    }

    override fun doDrawing(batch: Batch) {
        for (snake in snakes) {
            snake.doDrawing(batch)
        }
        batch.draw(multiFlash.getKeyFrame(timer), food.x.toFloat(), food.y.toFloat())
    }

//    private fun drawText(batch: Batch) {
//        font.color = if (timeleft() > 21) Color.WHITE else redFlash.getKeyFrame(timer, true)
//        font.draw(batch, "${timeleft()}", 150f, 240f)
//        font.color = Color.WHITE
//        font.draw(batch, "L${friendlyLevelNumber()}", 0f, 240f)
//        font.draw(batch, "$scoreDisplay PTS", 0f, 240f, 320f, Align.right, false)
//    }

//        val s = "\n\n\nLEVEL ${friendlyLevelNumber()}\n\n${endOfLevelMessage}\n" +
//                scoreTable() +
//                "\n\n\nTOTAL SCORE ${players.sumBy { it.score }}"
//
//        font.draw(batch, s, 0f, 240f, 320f, Align.center, false)
//    }

//    fun scoreTable() = players.joinToString("") {
//        (if (it == winner) "[${multiFlash.getKeyFrame(timer, true)}]" else "") +
//                "\n\n${it.name} ${it.score}[]"
//    }

//    private fun timeleft(): Int {
//        return (timeLimit - timer).toInt()
//    }

    override fun show() {
        if (Prefs.BinPref.MUSIC.isEnabled()) music.play()
        App.app.manualGC?.disable()
    }

    override fun hide() {
        music.stop()
        App.app.manualGC?.enable()
        App.app.manualGC?.doGC()
    }

    override fun dispose() {
        jumpSound.dispose()
        stunSound.dispose()
        bonusSound.dispose()
        spawnSound.dispose()
        music.dispose()
    }
}
