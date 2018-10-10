package uk.co.electronstudio.snakeeaters

import uk.me.fantastic.retro.AbstractGameFactory
import uk.me.fantastic.retro.App
import uk.me.fantastic.retro.Game
import uk.me.fantastic.retro.screens.GameSession

/*
 * Used by RetroWar to create our main class
 */
class SnakeFactory(path: String) : AbstractGameFactory("Snake", null, path) {



    override val description = "A snake game. Eat the apples, grow as large as you can and trip up other players!"

    override fun create(session: GameSession): Game {
        App.app.configureSessionWithPreSelectedInputDevice(session)
        return SnakeGame(session)
    }

    override fun createWithDefaultSettings(session: GameSession): Game {
        return SnakeGame(session)
    }
}
