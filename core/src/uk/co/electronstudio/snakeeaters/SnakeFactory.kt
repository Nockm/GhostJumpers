package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import uk.me.fantastic.retro.AbstractGameFactory
import uk.me.fantastic.retro.App
import uk.me.fantastic.retro.Game
import uk.me.fantastic.retro.screens.GameSession

/*
 * Used by RetroWar to create our main class
 */
class SnakeFactory : AbstractGameFactory("Snake", null) {

    override val image by lazy { Texture(Gdx.files.internal("addons/GhostJumpers/pimpenemy.png")) }

    override val description = "A snake game. Eat the apples, grow as large as you can and trip up other players!"

    override fun create(session: GameSession): Game {
        App.app.configureSessionWithPreSelectedInputDevice(session)
        return SnakeGame(session)
    }

    override fun createWithDefaultSettings(session: GameSession): Game {
        return SnakeGame(session)
    }
}
