package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.Align
import uk.co.electronstudio.retrowar.App
import uk.co.electronstudio.retrowar.LevelLoader
import uk.co.electronstudio.retrowar.Player
import uk.co.electronstudio.retrowar.SimpleGame
import uk.co.electronstudio.retrowar.screens.GameSession
import uk.co.electronstudio.snakeeaters.SnakeGame.Companion.INVULNERABLE_PERIOD
import uk.co.electronstudio.snakeeaters.SnakeGame.Companion.PAUSE_TIME

/* The God class */
class SnakeGame(session: GameSession, val pathPrefix: String, val suddenDeath: Boolean,
                val maxFoods: Int, val minFoods: Int, val foodGoal: Int, var speed: Float,
                val speedup: Boolean = false, val foodValue: Int, val levelFiles: List<LevelLoader>,
                val levelIndex: Int, val maxLevelsToPlay: Int) :
    SimpleGame(session, 88f, 50f, BitmapFont(Gdx.files.internal(pathPrefix + "5pix.fnt")), false) {
    companion object {
        val INVULNERABLE_PERIOD = 10f
        val PAUSE_TIME = 5f
    }

    var arena = Arena(this, levelFiles[levelIndex].file)
    private var winner: Snake? = null
    private var overallWinner: Player? = null

    val jumpSound = Gdx.audio.newSound(Gdx.files.internal(pathPrefix + "jump_jade.wav"))
    val stunSound = Gdx.audio.newSound(Gdx.files.internal(pathPrefix + "fall_jade.wav"))
    val bonusSound = Gdx.audio.newSound(Gdx.files.internal(pathPrefix + "bonus_jade.wav"))
    val moveSound = Gdx.audio.newSound(Gdx.files.internal(pathPrefix + "move2.wav"))
    //    val music = CrossPlatformMusic.create(desktopFile = pathPrefix + "justin1.ogg", androidFile =
    //    pathPrefix + "JustinLong.ogg", iOSFile = pathPrefix + "justin1.wav")
    val multiFlash =
        Animation<Texture>(1f / 30f, makePixel(Color.RED), makePixel(Color.BLUE), makePixel(Color.GREEN)).also {
            it.playMode = Animation.PlayMode.LOOP
        }


    var timer = 0f
    var tickTimer = 0f

    fun delay() = 1f / 60f / speed

    val snakes = ArrayList<Snake>()
    val foods = MutableList(maxFoods) { arena.getRandomEmptyPoint() }

    var state = State.PLAYING

    enum class State {
        PLAYING, GAMEOVER, LEVEL_COMPLETED, GAME_COMPLETED
    }

    fun tick() {
        moveSound.play()
        for (snake in snakes) {
            snake.move()
        }

        val deadSnakes = ArrayList<Snake>()
        val deadFoods = ArrayList<Point>()

        for (snake1 in snakes) {
            for (food in foods) {
                if (snake1.hasCollidedWith(food)) {
                    bonusSound.play()
                    deadFoods.add(food)
                    snake1.maxLength += foodValue
                    snake1.player.score++
                }
            }
            if(timer>INVULNERABLE_PERIOD){
                for (snake2 in snakes) {
                    if (snake1.hasCollidedWith(snake2)) {
                        snake1.doCollision()
                        if (snake1.maxLength < 1 || suddenDeath) {
                            deadSnakes.add(snake1)
                        }
                    }
                }
                if (!arena.emptyCellAt(snake1.head)) {
                    snake1.doCollision()
                    if (snake1.maxLength < 1 || suddenDeath) {
                        deadSnakes.add(snake1)
                    }
                }
            }
            if (snake1.maxLength > foodGoal) {
                winner = snake1
                if (thereAreMoreLevelsToPlay()) {
                    state=State.LEVEL_COMPLETED
                    session.nextGame = SnakeGame(session,
                        pathPrefix,
                        suddenDeath,
                        maxFoods,
                        minFoods,
                        foodGoal,
                        speed,
                        speedup,
                        foodValue,
                        levelFiles,
                        levelIndex + 1,
                        maxLevelsToPlay - 1)
                }else{
                    state=State.GAME_COMPLETED

                    overallWinner = session.findWinners().first()
                    overallWinner?.incMetaScore()
                }
            }
        }

        foods.removeAll(deadFoods)
        while (foods.size < minFoods) {
            foods.add(arena.getRandomEmptyPoint())
        }
        deadFoods.clear()

        if(deadSnakes.isNotEmpty()) {
            snakes.removeAll(deadSnakes)
            deadSnakes.clear()

            if (snakes.isEmpty()) {
                state = State.GAMEOVER
                overallWinner = session.findWinners().first()
                overallWinner?.incMetaScore()
            }
        }


        if (speedup) {
            speed += 0.0005f
        }
    }

    override fun playerJoined(player: Player) {
        snakes.add(Snake(this, player, Direction.SOUTH, arena.getRandomEmptyPoint(), 2))
    }

    override fun doLogic(deltaTime: Float) {
        timer += deltaTime
        tickTimer += deltaTime

        if(session.state==GameSession.GameState.MENU) return

        when (state) {
            State.PLAYING -> {
                if (tickTimer > delay()) {
                    tickTimer = 0f
                    tick()
                }
                for (snake in snakes) {
                    snake.doInput()
                }
            }
            State.GAMEOVER -> {
                if (tickTimer > PAUSE_TIME) gameover()
            }
            State.LEVEL_COMPLETED -> {
                if (tickTimer > PAUSE_TIME) {
                    gameover()
                }
            }
            State.GAME_COMPLETED -> {
                if (tickTimer > PAUSE_TIME) {
                    gameover()
                }
            }
        }




    }

    private fun thereAreMoreLevelsToPlay() = levelIndex < levelFiles.lastIndex && maxLevelsToPlay >= 2

    val flicker = Animation<Boolean>(1f/15, true, false)

    override fun doDrawing(batch: Batch) {
        if(state==State.PLAYING && (!flicker.getKeyFrame(timer, true) || timer > 1f)) {
            arena.doDrawing(batch)
        }

        if(timer > INVULNERABLE_PERIOD || flicker.getKeyFrame(timer, true)) {
            for (snake in snakes) {
                snake.doDrawing(batch)
            }
        }

        drawScores(batch)
        for (food in foods) {
            batch.draw(multiFlash.getKeyFrame(timer),
                food.x.toFloat() + arena.xOffset,
                food.y.toFloat() + arena.yOffset)
        }
        winner?.let {
            font.color = it.player.color2
            font.draw(batch, "\nWinner\n\n${it.player.name}", 0f, height, width, Align.center, false)
        }
        if (state==State.GAMEOVER) {
            font.color = Color.WHITE
            font.draw(batch, "GameOver", 0f, height / 2, width, Align.center, false)
        }
        overallWinner?.let {
            font.color = Color.WHITE
            font.draw(batch, "\n\nOverall Winner\n${it.name}", 0f, height / 2, width, Align.center, false)
        }
    }

    private fun drawScores(batch: Batch) {
        var y = height
        players.forEachIndexed { index, player ->
            font.color = findGoodColor(player.color2, player.color)
            if (index % 2 == 0) {
                font.draw(batch, player.score.toString(), 2f, y)
            } else {
                font.draw(batch, player.score.toString(), 80f, y)
                y -= 8f
            }
        }
    }

    override fun show() {
        // if (Prefs.BinPref.MUSIC.isEnabled()) music.play()
        App.app.manualGC?.disable()
        session.nextGame = null
    }

    override fun hide() {
        //music.stop()
        App.app.manualGC?.enable()
        App.app.manualGC?.doGC()
    }

    override fun dispose() {
        jumpSound.dispose()
        stunSound.dispose()
        bonusSound.dispose()
        moveSound.dispose()
        // music.dispose()
    }
}

fun makePixel(color: Color): Texture {
    val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
    pixmap.setColor(color)
    pixmap.drawPixel(0, 0)
    val texture = Texture(pixmap)
    texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
    return Texture(pixmap)
}


