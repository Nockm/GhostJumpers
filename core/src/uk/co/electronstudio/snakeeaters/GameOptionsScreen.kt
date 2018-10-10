package uk.co.electronstudio.snakeeaters

import com.badlogic.gdx.graphics.g2d.Batch
import uk.me.fantastic.retro.AbstractGameFactory
import uk.me.fantastic.retro.App
import uk.me.fantastic.retro.menu.ActionMenuItem
import uk.me.fantastic.retro.menu.EmptyMenuItem
import uk.me.fantastic.retro.menu.Menu
import uk.me.fantastic.retro.menu.MenuController
import uk.me.fantastic.retro.menu.MultiChoiceMenuItem
import uk.me.fantastic.retro.screens.GameSession
import uk.me.fantastic.retro.screens.MenuScreen

class GameOptionsScreen(val game: AbstractGameFactory) : MenuScreen(true) {

    val optionsMenu = Menu("", doubleSpaced = true, quitAction = {
        App.app.showTitleScreen()
    })

    val menuStart = ActionMenuItem("CONTINUE", action = {
        var factory = game
        App.app.screen = GameSession(factory = factory)
    })

    override val controller = MenuController(optionsMenu, WIDTH, HEIGHT, x = 0f, y = HEIGHT - 20f)



    init {
        with(optionsMenu) {
            addAll(game.options)
            add(EmptyMenuItem())
            addAndSelect(menuStart)
        }
    }

    override fun additionalRendering(batch: Batch) {
    }
}
