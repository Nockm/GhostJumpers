package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import uk.me.fantastic.retro.App
import uk.me.fantastic.retro.Prefs
import uk.me.fantastic.retro.SimpleGame
import uk.me.fantastic.retro.screens.GameSession


/* The God class */
class SnakeGame(session: GameSession) :
        SimpleGame(session,
                50f, 50f, font, font, false) {

    override val MAX_FPS = 250f
    override val MIN_FPS = 20f

    companion object {
        private val font = BitmapFont(Gdx.files.internal("addons/GhostJumpers/c64_low3_black.fnt")) // for drawing text
        var arena = Rectangle()
    }

    val jumpSound = Gdx.audio.newSound(Gdx.files.internal("addons/GhostJumpers/jump_jade.wav"))
    val stunSound = Gdx.audio.newSound(Gdx.files.internal("addons/GhostJumpers/fall_jade.wav"))
    val bonusSound = Gdx.audio.newSound(Gdx.files.internal("addons/GhostJumpers/bonus_jade.wav"))
    val spawnSound = Gdx.audio.newSound(Gdx.files.internal("addons/GhostJumpers/hit_jade.wav"))
    val music = CrossPlatformMusic.create(desktopFile = "addons/GhostJumpers/justin1.ogg", androidFile =
    "addons/GhostJumpers/JustinLong.ogg", iOSFile = "addons/GhostJumpers/justin1.wav")

    val redFlash = Animation<Color>(0.1f, Color.BLACK, Color.RED)
    val multiFlash = Animation<String>(1f / 30f, "RED", "PURPLE", "BLUE", "CYAN", "GREEN", "YELLOW")

    var noOfPlayersInGameAlready = 0
    var timer = 0f
    var delay = 0.4f

    val snakes = ArrayList<Snake>()
    var food : Point

    init {
        font.data.markupEnabled = true
        arena = Rectangle(0f,0f,width,height)
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

    override fun doLogic(deltaTime: Float) {
        for (i in noOfPlayersInGameAlready until players.size) { // loop only when there is a new player(s) joined
            noOfPlayersInGameAlready++
            snakes.add(Snake(players[i], players[i].color2, Direction.SOUTH, getRandomPoint()))
        }

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
