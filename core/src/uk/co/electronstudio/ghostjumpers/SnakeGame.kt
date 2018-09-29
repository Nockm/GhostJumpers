package uk.co.electronstudio.ghostjumpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Align
import javafx.scene.image.PixelFormat
import uk.me.fantastic.retro.App
import uk.me.fantastic.retro.Player
import uk.me.fantastic.retro.Prefs
import uk.me.fantastic.retro.SimpleGame
import uk.me.fantastic.retro.isMobile
import uk.me.fantastic.retro.renderTileMapToTexture
import uk.me.fantastic.retro.screens.GameSession

/* The God class */
class SnakeGame(session: GameSession) :
        SimpleGame(session,
                100f, 100f, font, font, false) {

    override val MAX_FPS = 250f
    override val MIN_FPS = 20f

    companion object {
        private val font = BitmapFont(Gdx.files.internal("addons/GhostJumpers/c64_low3_black.fnt")) // for drawing text
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

    val dot: Texture
    init {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.PINK)
        pixmap.drawPixel(0,0)
        dot = Texture(pixmap)
        font.data.markupEnabled = true
    }

    override fun doLogic(deltaTime: Float) {
        timer += deltaTime
        doGameLogic(deltaTime)
    }

    private fun doGameoverLogic() {
//        if (timer > 1f && players.any { it.input.fire }) {
//        }
    }

    private fun doGameLogic(delta: Float) {
        checkForNewPlayerJoins()
    }

    override fun doDrawing(batch: Batch) {
        println("It's doing it")
        batch.draw(dot, 30f, 30f)
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

    private fun checkForNewPlayerJoins() {
        for (i in noOfPlayersInGameAlready until players.size) { // loop only when there is a new player(s) joined
            noOfPlayersInGameAlready++
        }
    }

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
